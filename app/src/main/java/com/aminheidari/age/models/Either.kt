package com.aminheidari.age.models

sealed class Either<out T> {
    data class Success<T>(val value: T) : Either<T>()
    data class Failure(val e: Exception) : Either<Nothing>()
}