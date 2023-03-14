@file:Suppress("DEPRECATION")

package me.jeffreychang.weatherapp.util

import android.content.Context
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class LatLng(val lat: Double, val lng: Double)
interface LocationProvider {

    suspend fun getAddress(latLng: LatLng): String

    fun getCountryCode(): String

    fun getLanguageCode(): String

}

class AndroidLocationProvider @Inject constructor(
    private val context: Context,
    private val contextProvider: ContextProvider
) : LocationProvider {
    override suspend fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(context)
        return withContext(contextProvider.io) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCoroutine { continuation ->
                    geocoder.getFromLocation(latLng.lat, latLng.lng, 1) {
                        continuation.resume(it.first().locality)
                    }
                }
            } else {
                geocoder.getFromLocation(latLng.lng, latLng.lng, 1)?.first()?.locality.orEmpty()
            }
        }
    }

    override fun getCountryCode(): String {
        return context.resources.configuration.locale.country
    }

    override fun getLanguageCode(): String {
        return context.resources.configuration.locale.language
    }
}

