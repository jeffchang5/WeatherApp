package me.jeffreychang.weatherapp.data.location

import kotlinx.coroutines.withContext
import me.jeffreychang.weatherapp.feature.currentweather.LocationDao
import me.jeffreychang.weatherapp.model.dto.Location
import me.jeffreychang.weatherapp.util.ContextProvider

interface LocationRepository {

    suspend fun putLocation(location: Location)

    suspend fun getLocation(): Location

}

class LocalLocationRepository constructor(
    private val contextProvider: ContextProvider,
    private val locationDao: LocationDao
) : LocationRepository {

    override suspend fun putLocation(location: Location) {
        withContext(contextProvider.io) {
            locationDao.insert(location)
        }
    }

    override suspend fun getLocation(): Location {
        return withContext(contextProvider.io) {
            locationDao.getLocation()
        }
    }
}