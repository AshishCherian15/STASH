package com.ashish.stash.core.util

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable, val userMessage: String) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}
