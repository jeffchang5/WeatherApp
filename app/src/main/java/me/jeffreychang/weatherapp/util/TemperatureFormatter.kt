package me.jeffreychang.weatherapp.util

import android.icu.text.MeasureFormat
import android.icu.text.MeasureFormat.FormatWidth
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import java.util.*
import kotlin.math.roundToInt


class TemperatureFormatter {

    private val formatter: MeasureFormat =
        MeasureFormat.getInstance(Locale.getDefault(), FormatWidth.SHORT)

    enum class Unit {
        F, C
    }

    private var unit: Unit? = Unit.F

    fun format(kelvin: Double): String {
        val measureUnit = if (unit == Unit.F) {
            MeasureUnit.FAHRENHEIT
        } else MeasureUnit.CELSIUS

        val temperature = if (unit == Unit.F) {
            toFahrenheit(kelvin)
        } else {
            toCelsius(kelvin)
        }
        val measure = Measure(temperature.roundToInt(), measureUnit)
        return formatter.format(measure)
    }

    private fun toFahrenheit(K: Double): Double {
        return (K - 273.15) * 9 / 5 + 32
    }

    private fun toCelsius(K: Double): Double {
        return K - 273.15
    }
}