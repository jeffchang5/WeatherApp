package me.jeffreychang.weatherapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import me.jeffreychang.weatherapp.feature.weather.OpenWeatherRepository
import me.jeffreychang.weatherapp.feature.weather.WeatherRepository
import me.jeffreychang.weatherapp.data.weather.WeatherDao
import me.jeffreychang.weatherapp.data.weather.WeatherService
import me.jeffreychang.weatherapp.data.location.LocalLocationRepository
import me.jeffreychang.weatherapp.data.location.LocationRepository
import me.jeffreychang.weatherapp.data.location.LocationDao
import me.jeffreychang.weatherapp.util.AndroidLocationProvider
import me.jeffreychang.weatherapp.util.ContextProvider
import me.jeffreychang.weatherapp.util.LocationProvider
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class CurrentWeatherModule {

    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    fun provideWeatherRepository(
        contextProvider: ContextProvider,
        weatherService: WeatherService,
        weatherDao: WeatherDao
    ): WeatherRepository {
        return OpenWeatherRepository(contextProvider, weatherService, weatherDao)
    }

    @Provides
    fun provideLocationRepository(
        contextProvider: ContextProvider,
        locationDao: LocationDao
    ): LocationRepository {
        return LocalLocationRepository(contextProvider, locationDao)
    }

    @Provides
    fun provideLocationProvider(
        @ApplicationContext context: Context,
        contextProvider: ContextProvider
    ): LocationProvider {
        return AndroidLocationProvider(context, contextProvider)
    }
}