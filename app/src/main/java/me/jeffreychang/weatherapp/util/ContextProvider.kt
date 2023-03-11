package me.jeffreychang.weatherapp.util

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface ContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
    val default: CoroutineContext
}

object AppContextProvider : ContextProvider {
    override val main: CoroutineContext
        get() = Dispatchers.Main
    override val io: CoroutineContext
        get() = Dispatchers.IO
    override val default: CoroutineContext
        get() = Dispatchers.Default
}