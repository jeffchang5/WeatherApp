package me.jeffreychang.weatherapp.feature.weather

import android.os.Bundle
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import me.jeffreychang.weatherapp.R
import me.jeffreychang.weatherapp.util.TemperatureFormatter


val formatter = TemperatureFormatter()

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val finalHost = NavHostFragment.create(R.navigation.nav_weather)

        val view = FragmentContainerView(this)
        val id = View.generateViewId()
        view.id = id

        setContentView(
            view,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        supportFragmentManager.beginTransaction()
            .replace(id, finalHost)
            .setPrimaryNavigationFragment(finalHost)
            .commit()
    }
}

