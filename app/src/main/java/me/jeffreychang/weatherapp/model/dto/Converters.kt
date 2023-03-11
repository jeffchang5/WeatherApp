package me.jeffreychang.weatherapp.model.dto

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.jeffreychang.weatherapp.model.onecall.OneShotWeather


class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromJson(value: String?): OneShotWeather? {
        if (value != null)
            return json.decodeFromString(value)
        return null
    }

    @TypeConverter
    fun toJson(oneShotWeather: OneShotWeather?): String? {
        if (oneShotWeather != null)
            return json.encodeToString(oneShotWeather)
        return null
    }
}