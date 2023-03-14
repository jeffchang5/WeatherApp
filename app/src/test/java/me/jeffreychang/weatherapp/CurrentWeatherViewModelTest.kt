package me.jeffreychang.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.*
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import me.jeffreychang.weatherapp.data.location.LocationRepository
import me.jeffreychang.weatherapp.feature.weather.*
import me.jeffreychang.weatherapp.model.dto.Location
import me.jeffreychang.weatherapp.util.LatLng
import me.jeffreychang.weatherapp.util.LocationProvider
import me.jeffreychang.weatherapp.util.ResultOf
import org.junit.Test

import org.junit.Rule
import org.junit.rules.TestRule

class CurrentWeatherViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private val contextProvider = TestContextProvider

    private val testLocationProvider = object : LocationProvider {

        override suspend fun getAddress(latLng: LatLng): String {
            return "1 Mountain Drive"
        }

        override fun getCountryCode(): String {
            return "US"
        }

        override fun getLanguageCode(): String {
            return "en"
        }
    }

    private val testLocation = Location(
        0,
        "Los Angeles",
        0.0,
        0.0,
        "Los Angeles",
        "Los Angeles",
        "US"
    )


    @Test
    fun `Weather list with search results in Success UI state`() {
        runBlocking {
            val weatherRepo: WeatherRepository = mockk {
                coEvery { getWeather(any()) } returns ResultOf.Success(
                    testOneShotWeather
                )
            }

            val locationRepo = object : LocationRepository {
                override val searchLocation: Flow<Location>
                    get() = flow {
                        emit(testLocation)
                    }

                override suspend fun putLocation(location: Location) {

                }
            }

            val testGpsUseCase = GetCurrentWeatherWithGpsUseCase(weatherRepo, testLocationProvider)
            val searchUseCase = GetCurrentWeatherSearchUseCase(weatherRepo)
            val gpsLocationUseCase = spyk(testGpsUseCase)
            val searchUseCaseSpy = spyk(searchUseCase)


            val currentWeatherViewModel =
                CurrentWeatherViewModel(
                    contextProvider,
                    gpsLocationUseCase,
                    searchUseCaseSpy,
                    locationRepo
                )


            coVerify {
                searchUseCaseSpy.getCurrentWeather(testLocation)
            }

            assert(currentWeatherViewModel.uiState().value is UiState.Success)
        }
    }

    @Test
    fun `Search results doesn't emit`() {
        runBlocking {
            val weatherRepo: WeatherRepository = mockk {
                coEvery { getWeather(any()) } returns ResultOf.Success(
                    testOneShotWeather
                )
            }

            val locationRepo = object : LocationRepository {
                override val searchLocation: Flow<Location>
                    // don't emit
                    get() = flow {}

                override suspend fun putLocation(location: Location) {

                }
            }
            val testGpsUseCase = GetCurrentWeatherWithGpsUseCase(weatherRepo, testLocationProvider)
            val searchUseCase = GetCurrentWeatherSearchUseCase(weatherRepo)
            val gpsLocationUseCaseSpy = spyk(testGpsUseCase)
            val searchUseCaseSpy = spyk(searchUseCase)

            val currentWeatherViewModel =
                CurrentWeatherViewModel(
                    contextProvider,
                    gpsLocationUseCaseSpy,
                    searchUseCaseSpy,
                    locationRepo
                )

            coVerify(exactly = 0) {
                searchUseCaseSpy.getCurrentWeather(testLocation)
            }
            assert(currentWeatherViewModel.uiState().value !is UiState.Success)

            currentWeatherViewModel.getCurrentWeather(LatLng(0.0, 0.0))

            coVerify(exactly = 1) {
                gpsLocationUseCaseSpy.getCurrentWeather(any())
            }
            assert(currentWeatherViewModel.uiState().value is UiState.Success)
        }
    }

    @Test
    fun `fetching weather exception results in Error UI state`() {
        runBlocking {
            val locationRepo = object : LocationRepository {
                override val searchLocation: Flow<Location>
                    get() = flow {
                        emit(testLocation)
                    }

                override suspend fun putLocation(location: Location) {

                }
            }
            val weatherRepo: WeatherRepository = mockk {
                coEvery { getWeather(any()) } returns ResultOf.Failure(
                    "test error", Exception()
                )
            }

            val searchUseCase = GetCurrentWeatherWithGpsUseCase(weatherRepo, testLocationProvider)
            val testGpsUseCase = GetCurrentWeatherSearchUseCase(weatherRepo)

            val currentWeatherViewModel =
                CurrentWeatherViewModel(
                    contextProvider,
                    searchUseCase,
                    testGpsUseCase,
                    locationRepo
                )

            assert(currentWeatherViewModel.uiState().value is UiState.Error)
        }
    }
}