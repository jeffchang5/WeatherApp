package me.jeffreychang.weatherapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.jeffreychang.weatherapp.model.dto.WeatherDto
import me.jeffreychang.weatherapp.model.onecall.OneShotWeather

@Dao
interface WeatherDao {

    @Query("SELECT weather FROM ${WeatherDto.TABLE_NAME} LIMIT 1")
    fun getOneShotWeather(): OneShotWeather

    @Insert
    fun insert(oneShotWeather: WeatherDto)

    @Delete
    fun delete(user: WeatherDto)


}