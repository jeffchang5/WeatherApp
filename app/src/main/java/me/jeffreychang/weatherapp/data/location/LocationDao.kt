package me.jeffreychang.weatherapp.data.location

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.jeffreychang.weatherapp.model.dto.Location

@Dao
interface LocationDao {

    @Query("SELECT * FROM ${Location.TABLE_NAME} WHERE isPrimary = true LIMIT 1")
    fun getPrimaryLocation(): Flow<Location>

    @Query("SELECT * FROM ${Location.TABLE_NAME} WHERE isPrimary = false ORDER BY created_at DESC LIMIT 5")
    fun getRecentLocations(): Flow<List<Location>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: Location)

    @Query("UPDATE ${Location.TABLE_NAME} SET isPrimary = false")
    fun removePrimaryLocations()

    @Delete
    fun delete(location: Location)

}