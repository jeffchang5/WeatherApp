package me.jeffreychang.weatherapp.feature.weather

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.jeffreychang.weatherapp.data.location.LocationRepository
import me.jeffreychang.weatherapp.model.dto.WeatherDto
import me.jeffreychang.weatherapp.util.ContextProvider
import me.jeffreychang.weatherapp.util.LatLng
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val WEATHER_STATE = "WEATHER_STATE"

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
    private val getCurrentWeatherUseCase: GetCurrentWeatherWithGpsUseCase,
    private val getCurrentWeatherSearchUseCase: GetCurrentWeatherSearchUseCase,
    private val locationRepository: LocationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val savedWeather: LiveData<WeatherDto> =
        savedStateHandle.getLiveData<WeatherDto>(WEATHER_STATE)

    private val uiState = MutableLiveData<UiState>()

    init {
//        savedWeather.value?.let {
//            uiState.postValue(UiState.Success(it))
//        } ?: run {
//            getCurrentWeather(Location())
//        }
        viewModelScope.launch {
            locationRepository.localLocation.collect {
                tryGetWeather {
                    getCurrentWeatherSearchUseCase.getCurrentWeather(it)
                }
            }
        }
    }

    fun uiState(): LiveData<UiState> {
        return uiState
    }

    fun getCurrentWeather(latLng: LatLng) {
        tryGetWeather {
            getCurrentWeatherUseCase.getCurrentWeather(latLng)
        }
    }

    private fun tryGetWeather(block: suspend () -> WeatherDto) {
        launch {
            try {
                val weatherDto = block()
                uiState.value = UiState.Success(weatherDto)
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

