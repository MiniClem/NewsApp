package io.github.newsapp.utils

/**
 * State class used for example in a Flow
 */
sealed class DataState<out T> {
    data class Success<out R>(val data: R) : DataState<R>()
    data class Error(val throwable: Throwable) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}