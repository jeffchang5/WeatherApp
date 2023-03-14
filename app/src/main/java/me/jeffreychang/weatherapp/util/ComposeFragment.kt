package me.jeffreychang.weatherapp.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import me.jeffreychang.weatherapp.ui.theme.WeatherAppTheme

fun Fragment.composeView(block: @Composable () -> Unit): ComposeView {
    return ComposeView(requireActivity())
        .apply {
            setContent {
                WeatherAppTheme {
                    block()
                }
            }
        }
}
