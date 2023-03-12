package me.jeffreychang.weatherapp.data.location

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlinx.coroutines.withContext
import me.jeffreychang.weatherapp.feature.currentweather.LocationDao
import me.jeffreychang.weatherapp.model.dto.Location
import me.jeffreychang.weatherapp.util.ContextProvider

interface LocationRepository {

    val localLocation: Flow<Location>
    suspend fun putLocation(location: Location)

}

class LocalLocationRepository constructor(
    private val contextProvider: ContextProvider,
    private val locationDao: LocationDao
) : LocationRepository {

    override val localLocation get() = locationDao.getLocation()


    override suspend fun putLocation(location: Location) {
        withContext(contextProvider.io) {
            locationDao.insert(location.apply {
                isPrimary = true
            })
        }
    }
}