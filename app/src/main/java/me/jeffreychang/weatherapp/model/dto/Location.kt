package me.jeffreychang.weatherapp.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Location.TABLE_NAME)
data class Location(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val cityName: String,
    val lat: Double,
    val lon: Double,
    val englishName: String,
    val locality: String,
    val country: String,
    var isPrimary: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "location"
    }
}