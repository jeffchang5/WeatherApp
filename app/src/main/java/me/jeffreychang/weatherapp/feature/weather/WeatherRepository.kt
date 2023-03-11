package me.jeffreychang.weatherapp.feature.weather

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.jeffreychang.weatherapp.Constants
import me.jeffreychang.weatherapp.data.WeatherDao
import me.jeffreychang.weatherapp.data.WeatherService
import me.jeffreychang.weatherapp.model.geolocation.LocationDto
import me.jeffreychang.weatherapp.model.onecall.OneShotWeather
import me.jeffreychang.weatherapp.model.onecall.OneShotWeather.Companion.toDto
import me.jeffreychang.weatherapp.util.ContextProvider
import javax.inject.Inject

interface WeatherRepository {
    suspend fun getWeather(): OneShotWeather
    suspend fun getGeoCodeLocation(query: String): List<LocationDto>

}

class OpenWeatherRepository @Inject constructor(
    private val contextProvider: ContextProvider,
    private val weatherService: WeatherService,
    private val weatherDao: WeatherDao
) : WeatherRepository {

    override suspend fun getWeather(): OneShotWeather {
        return withContext(contextProvider.io) {
            try {
                weatherService.getOneCall("42.46", "-83.49", Constants.API_KEY).also {
                    weatherDao.insert(it.toDto())
                }
            } catch (t: Throwable) {
                weatherDao.getOneShotWeather()
            }
        }
    }

    override suspend fun getGeoCodeLocation(query: String): List<LocationDto> {
        return weatherService.getLocation(query, 5, Constants.API_KEY)
    }
}