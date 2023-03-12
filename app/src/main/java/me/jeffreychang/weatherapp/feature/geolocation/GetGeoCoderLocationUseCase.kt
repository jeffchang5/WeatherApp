package me.jeffreychang.weatherapp.feature.geolocation

import me.jeffreychang.weatherapp.feature.weather.WeatherRepository
import me.jeffreychang.weatherapp.model.dto.Location
import me.jeffreychang.weatherapp.util.LocationProvider
import me.jeffreychang.weatherapp.util.ResultOf
import timber.log.Timber
import javax.inject.Inject


class GetGeoCoderLocationUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider
) {
    suspend fun getGeoLocation(query: String): ResultOf<List<Location>> {
        return try {
            val geoLocation = weatherRepository.getGeoCodeLocation(query)
                .sortedBy {
                    if (it.country == locationProvider.getCountryCode()) {
                        0
                    } else 1
                }
                .filter { it.localNames?.en != null } // make sure value is not missing.
                .map { it.toUiModel() }
            ResultOf.Success(geoLocation)
        } catch (t: Throwable) {
            Timber.e(t)
            ResultOf.Failure("Failed to get any cities.", t)
        }
    }
}