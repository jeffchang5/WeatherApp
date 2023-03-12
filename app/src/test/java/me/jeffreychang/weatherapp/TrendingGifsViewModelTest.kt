package me.jeffreychang.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import me.jeffreychang.weatherapp.feature.weather.*
import me.jeffreychang.weatherapp.model.Gif
import org.junit.Test

import org.junit.Rule
import org.junit.rules.TestRule

class TrendingGifsViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private val gif1 = Gif()

    private val contextProvider = TestContextProvider

    @Test
    fun `Empty gifs list results in empty UI state`() {
        runBlocking {
            val gifRepo: WeatherRepository = mockk {
                coEvery { getWeather(location) } returns emptyList()
            }

            val useCase = GetCurrentWeatherWithGpsUseCase(gifRepo)

            val trendingGifsViewModel =
                CurrentWeatherViewModel(contextProvider, useCase, SavedStateHandle())

            assert(trendingGifsViewModel.uiState().value is UiState.Empty)
        }
    }

    @Test
    fun `Non-empty gifs list results in Success UI state`() {
        runBlocking {
            val gifRepo: WeatherRepository = mockk {
                coEvery { getWeather(location) } returns listOf(gif1)
            }

            val useCase = GetCurrentWeatherWithGpsUseCase(gifRepo)

            val gifsViewModel = CurrentWeatherViewModel(contextProvider, useCase, SavedStateHandle())

            assert(gifsViewModel.uiState().value is UiState.Success)
        }
    }

    @Test
    fun `Gifs in saved state handle results in Success UI state`() {
        runBlocking {
            val gifRepo: WeatherRepository = mockk {
                coEvery { getWeather(location) } returns listOf(gif1)
            }

            val useCase = GetCurrentWeatherWithGpsUseCase(gifRepo)

            val gifsViewModel =
                CurrentWeatherViewModel(contextProvider, useCase, SavedStateHandle().apply {
                    set(WEATHER_STATE, listOf(gif1))
                })


            verify(exactly = 0) {
                gifsViewModel.getCurrentWeather()
            }

            assert(gifsViewModel.uiState().value is UiState.Success)
        }
    }

    @Test
    fun `fetching gifs exception results in Error UI state`() {
        runBlocking {
            val gifRepo: WeatherRepository = mockk {
                coEvery { getWeather(location) } throws Exception()
            }

            val useCase = GetCurrentWeatherWithGpsUseCase(gifRepo)

            val gifsViewModel = CurrentWeatherViewModel(contextProvider, useCase, SavedStateHandle())

            assert(gifsViewModel.uiState().value is UiState.Error)
        }
    }
}