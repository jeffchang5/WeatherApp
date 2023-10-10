package me.jeffreychang.weatherapp.feature.geolocation

import androidx.lifecycle.*
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

    private var job: Job? = null

    fun locations(): LiveData<List<Location>> = locations

    fun recentLocations(): LiveData<List<Location>> =
        locationRepository.recentLocations.asLiveData()

    fun search(query: String) {
        debounce {
            when (val result = getGeoLocationUseCase.getGeoLocation(query)) {
                is ResultOf.Success -> {
                    locations.value = (result.value)
                }
                is ResultOf.Failure -> {
                    // TODO: create more unique UX for this, but right now. I wouldn't'
                    // want to flood the user with error messages
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

    // Only searches after time from keyboard input to avoid too many API calls.
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