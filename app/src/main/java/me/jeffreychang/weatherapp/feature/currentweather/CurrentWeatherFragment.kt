package me.jeffreychang.weatherapp.feature.currentweather


import android.icu.text.DateFormat
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.patrykandpatrick.vico.compose.axis.horizontal.topAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import dagger.hilt.android.AndroidEntryPoint
import me.jeffreychang.weatherapp.R
import me.jeffreychang.weatherapp.feature.weather.CurrentWeatherViewModel
import me.jeffreychang.weatherapp.feature.weather.UiState
import me.jeffreychang.weatherapp.feature.weather.formatter
import me.jeffreychang.weatherapp.model.dto.WeatherDto
import me.jeffreychang.weatherapp.model.onecall.Current
import me.jeffreychang.weatherapp.model.onecall.Hourly
import me.jeffreychang.weatherapp.model.onecall.Temp
import me.jeffreychang.weatherapp.ui.daily.DailyCard
import me.jeffreychang.weatherapp.ui.theme.Typography
import me.jeffreychang.weatherapp.util.GpsFragment
import me.jeffreychang.weatherapp.util.LatLng
import me.jeffreychang.weatherapp.util.composeView
import me.jeffreychang.weatherapp.util.rememberChartStyle
import me.jeffreychang.weatherapp.weatherDto
import timber.log.Timber

private val hourFormatter: DateFormat = DateFormat.getPatternInstance(DateFormat.HOUR_MINUTE)

@AndroidEntryPoint
class CurrentWeatherFragment : GpsFragment() {

    private val viewModel: CurrentWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return composeView {
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
            ) {
                CurrentWeather(viewModel)
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    FloatingActionButton(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            findNavController().navigate(R.id.searchCityFragment)
                        },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Add FAB",
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        askForLocation()
    }

    override fun onLocationPermissionGranted(location: Location) {
        viewModel.getCurrentWeather(LatLng(location.latitude, location.longitude))
    }

    override fun onLocationPermissionDeclined() {
        Toast.makeText(requireContext(), "Requires location permission", Toast.LENGTH_SHORT)
            .show()

        viewModel.onPermissionFailed()
    }
}

@Composable
fun CurrentWeather(
    viewModel: CurrentWeatherViewModel
) {
    val uiState by viewModel.uiState().observeAsState(UiState.Loading)

    when (val state = uiState) {
        is UiState.Loading -> {
            CenteredText(text = "Loading")
        }
        is UiState.Success -> {
            CurrentWeather(state.weatherDto)
        }
        is UiState.Error -> {
            Timber.e(state.t)
            CenteredText(text = "Error")
        }
        is UiState.LocationDenied -> {
            CenteredText(text = "Location Permission Denied")
        }
    }
}

@Composable
fun CenteredText(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text
        )
    }
}

private const val COLOR_1_CODE = 0xffff5500
private const val COLOR_2_CODE = 0xffd3d826
private val color1 = Color(COLOR_1_CODE)
private val chartColors = listOf(color1)

@Composable
fun CurrentWeather(weatherDto: WeatherDto) {
    Column {
        val weather = weatherDto.weather
        val temp = weather.daily.first().temp
        CurrentWeatherCard(weather.current, weatherDto.cityId, temp)
        Text(
            style = Typography.titleMedium, modifier = Modifier.padding(
                horizontal = 16.dp, vertical = 8.dp
            ), text = "Hourly"
        )
        val hours = weather.hourly.map {
            hourFormatter.format(it.getDate())
        }
        HourlyWeather(weather.hourly)
        Text(
            style = Typography.titleMedium, modifier = Modifier.padding(
                horizontal = 16.dp, vertical = 8.dp
            ), text = "Daily"
        )
        DailyCard(weather.daily)
        Text(
            style = Typography.titleMedium, modifier = Modifier.padding(
                horizontal = 16.dp, vertical = 8.dp
            ), text = "Precipitation"
        )
        PrecipitationCard(weather.hourly, hours)
    }
}

@Composable
fun PrecipitationCard(hourly: List<Hourly>, hours: List<String>) {
    class Entry(
        val localDate: String,
        override val x: Float,
        override val y: Float,
    ) : ChartEntry {
        override fun withY(y: Float) = Entry(localDate, x, y)
    }

    val rain = hourly.map { it.rain?.h?.toFloat() ?: 0f }
    val snow = hourly.map { it.snow?.h?.toFloat() ?: 0f }

    val rainSum = rain.sum()
    val snowSum = snow.sum()

    val entries = (if (rainSum > snowSum) rain else snow).mapIndexed { index, item ->
        Entry(
            hours[index],
            index.toFloat(),
            item
        )
    }
    val chartEntryModelProducer = ChartEntryModelProducer(entries)

    val axisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Top> { value, chartValues ->
        return@AxisValueFormatter ((chartValues.chartEntryModel.entries.first()
            .getOrNull(value.toInt())) as? Entry)?.localDate.orEmpty()
    }

    ProvideChartStyle(rememberChartStyle(chartColors)) {
        Chart(
            chart = lineChart(
                spacing = 48.dp
            ),
            chartModelProducer = chartEntryModelProducer,
            startAxis = startAxis(),
            topAxis = topAxis(
                valueFormatter = axisValueFormatter,
            ),
        )
    }
}

@Composable
fun HourlyWeather(hourly: List<Hourly>) {
    LazyRow {
        items(hourly) { item ->
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val weather = item.weather[0]
                val iconUrl = "https://openweathermap.org/img/wn/${weather.icon}.png"

                Text(
                    modifier = Modifier.padding(
                        vertical = 8.dp
                    ), text = hourFormatter.format(item.getDate())
                )
                AsyncImage(
                    modifier = Modifier
                        .height(36.dp)
                        .width(36.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    model = iconUrl,
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.baseline_error_24),
                    contentDescription = weather.description
                )
                Text(
                    modifier = Modifier.padding(16.dp), text = formatter.format(item.temp)
                )
            }
        }
    }
}

@Composable
fun CurrentWeatherCard(
    currentWeather: Current,
    cityId: String,
    temp: Temp
) {
    ElevatedCard(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        if (currentWeather.weather.getOrNull(0) == null) return@ElevatedCard
        val weather = currentWeather.weather[0]
        val iconUrl = "https://openweathermap.org/img/wn/${weather.icon}@2x.png"

        Row {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                weather.toResource().invoke()
                AsyncImage(
                    modifier = Modifier
                        .height(96.dp)
                        .width(96.dp)
                        .fillMaxSize(),
                    model = iconUrl,
                    contentScale = ContentScale.FillBounds,
                    error = painterResource(R.drawable.baseline_error_24),
                    contentDescription = currentWeather.weather.first().description
                )
                Text(
                    modifier = Modifier.padding(
                        vertical = 8.dp
                    ), text = weather.description
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(3f),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    style = Typography.headlineSmall, modifier = Modifier.padding(
                        horizontal = 16.dp, vertical = 8.dp
                    ), text = cityId
                )
                Text(
                    style = Typography.titleMedium, modifier = Modifier.padding(
                        horizontal = 16.dp, vertical = 8.dp
                    ), text = formatter.format(currentWeather.temp)
                )
                Text(
                    modifier = Modifier.padding(
                        horizontal = 16.dp, vertical = 8.dp
                    ), text = "feels like " + formatter.format(currentWeather.feelsLike)
                )
                Text(
                    modifier = Modifier.padding(
                        bottom = 16.dp
                    ), text = "L: ${formatter.format(temp.min)} H: ${formatter.format(temp.max)}"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrentWeather(weatherDto = weatherDto)
}