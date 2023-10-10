package me.jeffreychang.weatherapp.model.onecall


import androidx.compose.runtime.Composable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.jeffreychang.weatherapp.Sun

@Serializable
data class Weather(
    @SerialName("description")
    val description: String,
    @SerialName("icon")
    val icon: String,
    @SerialName("id")
    val id: Int,
    @SerialName("main")
    val main: String
) {
    @Composable
    fun toResource(): @Composable () -> Unit {
        return when (id) {
            800 -> ({ Sun() })
            else -> ({ Sun() })
        }
    }
}