package com.aminheidari.age.models

sealed class Either<out T> {
    data class success<T>(val data: T) : Either<T>()
    data class failure(val e: Throwable) : Either<Nothing>()
}