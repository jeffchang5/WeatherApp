package me.jeffreychang.weatherapp

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.jeffreychang.weatherapp.data.location.LocationDao
import me.jeffreychang.weatherapp.data.weather.WeatherDao
import me.jeffreychang.weatherapp.model.dto.Converters
import me.jeffreychang.weatherapp.model.dto.Location
import me.jeffreychang.weatherapp.model.dto.WeatherDto

@TypeConverters(Converters::class)
@Database(
    entities = [WeatherDto::class, Location::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    abstract fun locationDao(): LocationDao

}