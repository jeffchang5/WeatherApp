package me.jeffreychang.weatherapp.util

sealed class ResultOf<out T> {

    data class Success<out R>(val value: R) : ResultOf<R>()

    data class Failure(
        val message: String?,
        val throwable: Throwable?
    ) : ResultOf<Nothing>()

    fun <R> map(mapper: (value: T) -> R): ResultOf<R> {
        return when (this) {
            is Success -> {
                Success(mapper(value))
            }
            is Failure -> {
                Failure(message, throwable)
            }
        }
    }
}