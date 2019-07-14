package com.aminheidari.age.models

sealed class AppException: Throwable() {
    object Connection: AppException()
    object CertificateExpired: AppException()
    object Authentication: AppException()
    object Parsing: AppException()
    object IncorrectDeviceDateTime: AppException()
    object Unknown: AppException()
}