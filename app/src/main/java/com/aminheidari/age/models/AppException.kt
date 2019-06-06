package com.aminheidari.age.models

sealed class AppException: Throwable() {
    class Connection: AppException()
    class CertificateExpired: AppException()
    class Authentication: AppException()
    class Parsing: AppException()
    class Unknown: AppException()
}