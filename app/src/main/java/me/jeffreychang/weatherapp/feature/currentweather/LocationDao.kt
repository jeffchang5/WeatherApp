package me.jeffreychang.weatherapp.feature.currentweather

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.jeffreychang.weatherapp.model.dto.Location

@Dao
interface LocationDao {

    @Query("SELECT * FROM ${Location.TABLE_NAME} ORDER BY id DESC LIMIT 1")
    fun getLocation(): Flow<Location>

    @Insert
    fun insert(location: Location)

    @Delete
    fun delete(location: Location)

}