package me.jeffreychang.weatherapp.model.geolocation


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.jeffreychang.weatherapp.model.dto.Location

@Serializable
data class LocationDto(
    @SerialName("country")
    val country: String,
    @SerialName("lat")
    val lat: Double,
    @SerialName("local_names")
    val localNames: LocalNames? = null,
    @SerialName("lon")
    val lon: Double,
    @SerialName("name")
    val name: String,
    @SerialName("state")
    val state: String = ""
) {

    fun toUiModel(): Location {
        return Location(
            cityName = name,
            lat = lat,
            lon = lon,
            englishName = localNames?.en!!,
            locality = state,
            country = country
        )
    }
}