package me.jeffreychang.weatherapp.model.onecall


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Precipitation(
    @SerialName("1h")
    val h: Double
)