package me.jeffreychang.weatherapp.feature.geolocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.jeffreychang.weatherapp.data.location.LocationRepository
import me.jeffreychang.weatherapp.model.dto.Location
import me.jeffreychang.weatherapp.util.ResultOf
import javax.inject.Inject

@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val getGeoLocationUseCase: GetGeoCoderLocationUseCase,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val locations = MutableLiveData<List<Location>>()

    private val currentLocation = MutableLiveData<Location>()

    private var job: Job? = null

    fun locations(): LiveData<List<Location>> = locations

    fun currentLocation(): LiveData<Location> = currentLocation

    fun search(query: String) {
        debounce {
            when (val result = getGeoLocationUseCase.getGeoLocation(query)) {
                is ResultOf.Success -> {
                    locations.value = result.value
                }
                is ResultOf.Failure -> {
                    // TODO: handle
                }
            }
        }
    }

    fun updateLocation(location: Location, block: () -> Unit) {
        viewModelScope.launch {
            locationRepository.putLocation(location)
            block()
        }
    }

    private fun debounce(
        waitMs: Long = 300L,
        destinationFunction: suspend () -> Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            delay(waitMs)
            destinationFunction()
        }
    }
}