package me.jeffreychang.weatherapp.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.jeffreychang.weatherapp.feature.currentweather.LocationDao
import javax.inject.Inject

sealed class ScreenTransition {

    object CitySelected : ScreenTransition()

}

@HiltViewModel
class ScreenTransitionViewModel @Inject constructor() : ViewModel() {

    private val transitions = MutableLiveData<ScreenTransition>()

    fun transitions(): LiveData<ScreenTransition> = transitions

    fun transition(transition: ScreenTransition) {
        transitions.value = transition
    }
}