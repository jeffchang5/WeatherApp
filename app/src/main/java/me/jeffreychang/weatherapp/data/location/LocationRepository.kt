package me.jeffreychang.weatherapp.data.location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import me.jeffreychang.weatherapp.model.dto.Location
import me.jeffreychang.weatherapp.util.ContextProvider

interface LocationRepository {

    val searchLocation: Flow<Location>
    suspend fun putLocation(location: Location)

    val recentLocations: Flow<List<Location>>

}

class LocalLocationRepository constructor(
    private val contextProvider: ContextProvider,
    private val locationDao: LocationDao
) : LocationRepository {

    override val searchLocation get() = locationDao.getPrimaryLocation().filterNotNull()

    override val recentLocations: Flow<List<Location>>
        get() = locationDao.getRecentLocations()

    override suspend fun putLocation(location: Location) {
        withContext(contextProvider.io) {
            locationDao.removePrimaryLocations()
            locationDao.insert(location.apply {
                isPrimary = true
            })
        }
    }
}