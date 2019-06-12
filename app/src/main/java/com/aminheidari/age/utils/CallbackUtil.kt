package com.aminheidari.age.utils

sealed class Either<out T> {
    data class Success<T>(val data: T) : Either<T>()
    data class Failure(val exception: Throwable) : Either<Nothing>()
}

typealias Completion<T> = (result: Either<T>) -> Unit

typealias Finish<T> = (result: T) -> Unit