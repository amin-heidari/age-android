package com.aminheidari.age.utils

import com.aminheidari.age.models.AppException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException
import javax.net.ssl.SSLPeerUnverifiedException

data class FullHttpResponse<T>(
    val data: T,
    val response: Response<T>
)

fun <T>retrofitCallback(completion: Completion<T>): Callback<T> {
    return retrofitFullCallback { result ->
        when(result) {
            is Either.Failure -> {
                completion(Either.Failure(result.exception))
            }
            is Either.Success -> {
                completion(Either.Success(result.data.data))
            }
        }
    }
}

fun <T>retrofitFullCallback(completion: Completion<FullHttpResponse<T>>): Callback<T> {
    return object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            when (t) {
                is SSLPeerUnverifiedException -> {
                    completion(Either.Failure(AppException.CertificateExpired))
                }
                is UnknownHostException -> {
                    completion(Either.Failure(AppException.Connection))
                }
                else -> {
                    completion(Either.Failure(t))
                }
            }
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if ((200..299).contains(response.code())) {
                val body = response.body()
                if (body != null) {
                    completion(Either.Success(FullHttpResponse(body, response)))
                } else {
                    completion(Either.Failure(AppException.Parsing))
                }
            } else if (response.code() == 403) {
                completion(Either.Failure(AppException.Authentication))
            } else {
                completion(Either.Failure(AppException.Unknown))
            }
        }
    }
}