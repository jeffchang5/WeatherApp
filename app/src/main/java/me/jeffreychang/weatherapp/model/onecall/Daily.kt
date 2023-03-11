package me.jeffreychang.weatherapp.model.onecall


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Daily(
    @SerialName("clouds")
    val clouds: Int,
    @SerialName("dew_point")
    val dewPoint: Double,
    @SerialName("dt")
    val dt: Int,
    @SerialName("feels_like")
    val feelsLike: FeelsLike,
    @SerialName("humidity")
    val humidity: Int,
    @SerialName("moon_phase")
    val moonPhase: Double,
    @SerialName("moonrise")
    val moonrise: Int,
    @SerialName("moonset")
    val moonset: Int,
    @SerialName("pop")
    val pop: Double,
    @SerialName("pressure")
    val pressure: Int,
    @SerialName("rain")
    val rain: Double = 0.0,
    @SerialName("snow")
    val snow: Double = 0.0,
    @SerialName("sunrise")
    val sunrise: Int,
    @SerialName("sunset")
    val sunset: Int,
    @SerialName("temp")
    val temp: Temp,
    @SerialName("uvi")
    val uvi: Double,
    @SerialName("weather")
    val weather: List<Weather>,
    @SerialName("wind_deg")
    val windDeg: Int,
    @SerialName("wind_gust")
    val windGust: Double,
    @SerialName("wind_speed")
    val windSpeed: Double
) {
    fun getDate(): Long {
        return dt * 1000L
    }
}