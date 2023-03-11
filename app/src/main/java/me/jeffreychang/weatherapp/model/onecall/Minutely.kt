package me.jeffreychang.weatherapp.model.onecall


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Minutely(
    @SerialName("dt")
    val dt: Int,
    @SerialName("precipitation")
    val precipitation: Double
)