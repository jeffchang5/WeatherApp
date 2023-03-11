package me.jeffreychang.weatherapp.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import me.jeffreychang.weatherapp.ui.theme.WeatherAppTheme

abstract class ComposeFragment : Fragment() {

    @Composable
    abstract fun ComposeView()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity())
            .apply {
                setContent {
                    WeatherAppTheme {
                        ComposeView()
                    }
                }
            }
    }
}