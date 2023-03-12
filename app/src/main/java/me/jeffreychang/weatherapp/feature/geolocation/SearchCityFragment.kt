package me.jeffreychang.weatherapp.feature.geolocation


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.jeffreychang.weatherapp.R
import me.jeffreychang.weatherapp.data.location.LocationRepository
import me.jeffreychang.weatherapp.feature.ScreenTransition
import me.jeffreychang.weatherapp.feature.ScreenTransitionViewModel
import me.jeffreychang.weatherapp.feature.geolocation.GetGeoCoderLocationUseCase
import me.jeffreychang.weatherapp.feature.geolocation.SearchCityViewModel
import me.jeffreychang.weatherapp.feature.weather.WeatherRepository
import me.jeffreychang.weatherapp.model.dto.Location
import me.jeffreychang.weatherapp.model.geolocation.LocationDto
import me.jeffreychang.weatherapp.model.onecall.OneShotWeather
import me.jeffreychang.weatherapp.testOneShotWeather
import me.jeffreychang.weatherapp.util.ComposeFragment
import me.jeffreychang.weatherapp.util.LatLng
import me.jeffreychang.weatherapp.util.LocationProvider


@AndroidEntryPoint
class SearchCityFragment : ComposeFragment() {

    private val viewModel: SearchCityViewModel by viewModels()

    private val navViewModel: ScreenTransitionViewModel by navGraphViewModels(R.id.nav_weather)

    @Composable
    override fun ComposeView() {
        return SearchCityScreen(viewModel, ::navigate)
    }

    private fun navigate() {
        navViewModel.transition(ScreenTransition.CitySelected)
        findNavController().navigateUp()
    }
}

@Composable
fun SearchCityScreen(
    viewModel: SearchCityViewModel,
    transition: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column {
            CitySearchView(viewModel, transition)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchView(
    viewModel: SearchCityViewModel,
    transition: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    val locations by viewModel.locations().observeAsState(emptyList())

    TextField(value = text, onValueChange = {
        text = it
        viewModel.search(it)
    },
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(),
        modifier = Modifier
            .heightIn(56.dp)
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("City") }
    )
    LazyColumn {
        items(locations) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clickable {
                        val location = it
                        viewModel.updateLocation(location) {
                            // function block is make sure location is inserted before
                            // moving between screens.
                            transition()
                        }
                    },
                text = "${it.englishName}, ${it.locality}, ${it.country}",
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchCityScreen() {
    val weatherRepo = object : WeatherRepository {

        override suspend fun getWeather(latLng: LatLng): OneShotWeather {
            return testOneShotWeather
        }

        override suspend fun getGeoCodeLocation(query: String): List<LocationDto> {
            return emptyList()
        }
    }
    val testLocationProvider = object : LocationProvider {

        override suspend fun getAddress(latLng: LatLng): String {
            return ""
        }

        override fun getCountryCode(): String {
            return ""
        }

        override fun getLanguageCode(): String {
            return ""
        }
    }
    val repo = object : LocationRepository {
        override val localLocation: Flow<Location>
            get() = flow {
                emit(
                    Location(
                        0,
                        "Los Angeles",
                        0.0,
                        0.0,
                        "Los Angeles",
                        "Los Angeles",
                        "US"
                    )
                )
            }

        override suspend fun putLocation(location: Location) {

        }

    }
    val viewModel =
        SearchCityViewModel(
            GetGeoCoderLocationUseCase(weatherRepo, testLocationProvider),
            repo
        )
    SearchCityScreen(viewModel = viewModel) {

    }
}
