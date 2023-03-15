package me.jeffreychang.weatherapp.data.location

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import me.jeffreychang.weatherapp.model.dto.Location
import me.jeffreychang.weatherapp.util.ContextProvider

interface LocationRepository {

    val searchLocation: Flow<Location>
    suspend fun putLocation(location: Location)

}

class LocalLocationRepository constructor(
    private val contextProvider: ContextProvider,
    private val locationDao: LocationDao
) : LocationRepository {

    override val searchLocation get() = locationDao.getPrimaryLocation().filterNotNull()

    override suspend fun putLocation(location: Location) {
        withContext(contextProvider.io) {
            locationDao.removePrimaryLocations()
            locationDao.insert(location.apply {
                isPrimary = true
            })
        }
    }
}