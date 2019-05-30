package com.aminheidari.age.models

sealed class AppException: Throwable() {
    class connection: AppException()
    class certificateExpired: AppException()
    class parsing: AppException()
}