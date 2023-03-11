package me.jeffreychang.weatherapp.feature.weather

import me.jeffreychang.weatherapp.model.dto.WeatherDto
import me.jeffreychang.weatherapp.model.onecall.OneShotWeather
import me.jeffreychang.weatherapp.util.LocationProvider
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val cityRepository: WeatherRepository,
    private val locationProvider: LocationProvider
) {
    suspend fun getCurrentWeather(): WeatherDto {
        val locality = locationProvider.getAddress()
        return weatherRepository.getWeather().toDto(cityName = locality)
    }

    private fun OneShotWeather.toDto(cityName: String): WeatherDto {
        return WeatherDto(0, this, cityName)
    }
}