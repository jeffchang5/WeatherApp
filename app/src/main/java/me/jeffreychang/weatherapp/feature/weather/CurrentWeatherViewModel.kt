package me.jeffreychang.weatherapp.feature.weather

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.jeffreychang.weatherapp.model.dto.WeatherDto
import me.jeffreychang.weatherapp.util.ContextProvider
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val GIPHY_STATE = "GIPHY_STATE"

enum class ErrorReason {
    NO_NETWORK, MALFORMED, UNKNOWN
}

sealed class UiState {

    data class Success(val weatherDto: WeatherDto) : UiState()

    object Empty : UiState()

    data class Error(val errorReason: ErrorReason, val t: Throwable) : UiState()

    object Loading : UiState()
}


@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val contextProvider: ContextProvider,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val savedGifs: LiveData<WeatherDto> =
        savedStateHandle.getLiveData<WeatherDto>(GIPHY_STATE)

    private val uiState = MutableLiveData<UiState>()

    init {
        savedGifs.value?.let {
            uiState.postValue(UiState.Success(it))
        } ?: run {
            getCurrentWeather()
        }
    }

    fun uiState(): LiveData<UiState> {
        return uiState
    }

    fun getCurrentWeather() {
        uiState.postValue(UiState.Loading)

        launch {
            try {
                val gifs = getCurrentWeatherUseCase.getCurrentWeather()
                uiState.value = UiState.Success(gifs)
            } catch (t: Throwable) {
                uiState.postValue(t.toError())
            }
        }
    }

    private fun Throwable.toError(): UiState.Error {
        return when (this) {
            is UnknownHostException -> UiState.Error(ErrorReason.NO_NETWORK, this)
//            is J -> UiState.Error(ErrorReason.MALFORMED, this)
            else -> {
                return UiState.Error(ErrorReason.UNKNOWN, this)
            }
        }
    }

    private fun launch(
        coroutineContext: CoroutineContext = contextProvider.main,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch(coroutineContext) { block() }
    }
}

