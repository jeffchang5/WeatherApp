package me.jeffreychang.weatherapp.feature.weather

import me.jeffreychang.weatherapp.model.dto.Location
import me.jeffreychang.weatherapp.model.dto.WeatherDto
import me.jeffreychang.weatherapp.util.LatLng
import javax.inject.Inject

import me.jeffreychang.weatherapp.model.onecall.OneShotWeather
import me.jeffreychang.weatherapp.util.ResultOf

class GetCurrentWeatherSearchUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend fun getCurrentWeather(location: Location): ResultOf<WeatherDto> {
        return weatherRepository
            .getWeather(LatLng(location.lat, location.lon))
            .map { it.toDto(location.cityName) }
    }

    private fun OneShotWeather.toDto(cityName: String): WeatherDto {
        return WeatherDto(
            id = 0,
            weather = this,
            cityId = cityName
        )
    }
}