package me.jeffreychang.weatherapp

import kotlinx.coroutines.Dispatchers
import me.jeffreychang.weatherapp.util.ContextProvider
import kotlin.coroutines.CoroutineContext

object TestContextProvider : ContextProvider {
    override val main: CoroutineContext
        get() = Dispatchers.Unconfined
    override val io: CoroutineContext
        get() = Dispatchers.Unconfined
    override val default: CoroutineContext
        get() = Dispatchers.Unconfined
}