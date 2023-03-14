package me.jeffreychang.weatherapp.model.onecall


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.jeffreychang.weatherapp.model.dto.WeatherDto

@Serializable
data class OneShotWeather(
    @SerialName("alerts")
    val alerts: List<Alert> = emptyList(),
    @SerialName("current")
    val current: Current,
    @SerialName("daily")
    val daily: List<Daily>,
    @SerialName("hourly")
    val hourly: List<Hourly>,
    @SerialName("lat")
    val lat: Double,
    @SerialName("lon")
    val lon: Double,
    @SerialName("minutely")
    val minutely: List<Minutely> = emptyList(),
    @SerialName("timezone")
    val timezone: String,
    @SerialName("timezone_offset")
    val timezoneOffset: Int,
) {
    companion object {
        fun OneShotWeather.toDto(): WeatherDto {
            return WeatherDto(weather = this, cityId = "")
        }
    }
}