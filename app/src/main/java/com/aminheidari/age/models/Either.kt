package com.aminheidari.age.models

sealed class Either<out T> {
    data class Success<T>(val data: T) : Either<T>()
    data class Failure(val exception: Throwable) : Either<Nothing>()
}