package me.jeffreychang.weatherapp.model.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.jeffreychang.weatherapp.model.onecall.OneShotWeather

@Entity(tableName = WeatherDto.TABLE_NAME)
data class WeatherDto(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "weather")
    val weather: OneShotWeather,
    @ColumnInfo(name = "cityId")
    val cityId: String
) {
    companion object {
        const val TABLE_NAME = "weather"
    }
}