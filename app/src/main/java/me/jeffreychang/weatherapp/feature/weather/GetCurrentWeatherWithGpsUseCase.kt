package me.jeffreychang.weatherapp.feature.weather

import me.jeffreychang.weatherapp.model.dto.WeatherDto
import me.jeffreychang.weatherapp.model.onecall.OneShotWeather
import me.jeffreychang.weatherapp.util.LatLng
import me.jeffreychang.weatherapp.util.LocationProvider
import me.jeffreychang.weatherapp.util.ResultOf
import javax.inject.Inject

class GetCurrentWeatherWithGpsUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository, private val locationProvider: LocationProvider
) {
    suspend fun getCurrentWeather(latLng: LatLng): ResultOf<WeatherDto> {
        val locality = locationProvider.getAddress(latLng)
        return weatherRepository.getWeather(latLng).map {
                it.toDto(cityName = locality)
            }
    }

    private fun OneShotWeather.toDto(cityName: String): WeatherDto {
        return WeatherDto(0, this, cityName)
    }
}