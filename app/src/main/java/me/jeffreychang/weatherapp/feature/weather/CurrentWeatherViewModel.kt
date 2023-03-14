package me.jeffreychang.weatherapp.feature.weather

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import me.jeffreychang.weatherapp.data.location.LocationRepository
import me.jeffreychang.weatherapp.model.dto.WeatherDto
import me.jeffreychang.weatherapp.util.ContextProvider
import me.jeffreychang.weatherapp.util.LatLng
import me.jeffreychang.weatherapp.util.ResultOf
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
    private val getCurrentWeatherGpsUseCase: GetCurrentWeatherWithGpsUseCase,
    private val getCurrentWeatherSearchUseCase: GetCurrentWeatherSearchUseCase,
    private val locationRepository: LocationRepository,
) : ViewModel() {

    private val uiState = MutableLiveData<UiState>()

    init {
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
            getCurrentWeatherGpsUseCase.getCurrentWeather(latLng)
        }
    }

    private fun tryGetWeather(block: suspend () -> ResultOf<WeatherDto>) {
        launch {
            when (val weatherDto = block()) {
                is ResultOf.Success -> {
                    uiState.value = UiState.Success(weatherDto.value)
                }
                is ResultOf.Failure -> {
                    uiState.postValue(weatherDto.throwable?.toError())
                }
            }
        }
    }

    private fun Throwable.toError(): UiState.Error {
        return when (this) {
            is UnknownHostException -> UiState.Error(ErrorReason.NO_NETWORK, this)
            is SerializationException -> UiState.Error(ErrorReason.MALFORMED, this)
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

