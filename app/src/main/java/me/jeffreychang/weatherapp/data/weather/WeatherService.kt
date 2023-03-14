package me.jeffreychang.weatherapp.data.weather

import me.jeffreychang.weatherapp.model.geolocation.LocationDto
import me.jeffreychang.weatherapp.model.onecall.OneShotWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/3.0/onecall")
    suspend fun getOneCall(
        @Query("lat") lat: String, @Query("lon") long: String, @Query("appid") apiKey: String
    ): OneShotWeather

    @GET("/geo/1.0/direct")
    suspend fun getLocation(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): List<LocationDto>
}