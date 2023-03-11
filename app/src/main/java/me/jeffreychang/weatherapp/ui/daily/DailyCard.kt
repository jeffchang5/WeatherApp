package me.jeffreychang.weatherapp.ui.daily

import android.icu.text.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.jeffreychang.weatherapp.R
import me.jeffreychang.weatherapp.feature.weather.formatter
import me.jeffreychang.weatherapp.model.onecall.Daily

private val dayOfWeekFormatter: DateFormat = DateFormat.getPatternInstance(DateFormat.ABBR_WEEKDAY)

@Composable
fun DailyCard(daily: List<Daily>) {
    LazyRow {
        items(daily) { item ->
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val weather = item.weather[0]
                val iconUrl = "https://openweathermap.org/img/wn/${weather.icon}.png"

                Text(
                    modifier = Modifier.padding(
                        vertical = 8.dp
                    ), text = dayOfWeekFormatter.format(item.getDate())
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
                    modifier = Modifier.padding(16.dp), text = formatter.format(item.temp.day)
                )
            }
        }
    }
}