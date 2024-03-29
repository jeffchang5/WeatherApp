package me.jeffreychang.weatherapp.model.dto

import androidx.room.TypeConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.jeffreychang.weatherapp.model.onecall.OneShotWeather


class Converters {

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

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