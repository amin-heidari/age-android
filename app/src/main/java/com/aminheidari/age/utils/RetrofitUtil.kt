package com.aminheidari.age.utils

import com.aminheidari.age.models.AppException
import com.aminheidari.age.models.Completion
import com.aminheidari.age.models.Either
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException
import javax.net.ssl.SSLPeerUnverifiedException

fun <T>retrofitCallback(completion: Completion<T>): Callback<T> {
    return object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            when (t) {
                is SSLPeerUnverifiedException -> {
                    completion(Either.failure(AppException.certificateExpired()))
                }
                is UnknownHostException -> {
                    completion(Either.failure(AppException.connection()))
                }
                else -> {
                    completion(Either.failure(t))
                }
            }
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            val body = response.body()
            if (body != null) {
                completion(Either.success(body))
            } else {
                completion(Either.failure(AppException.parsing()))
            }
        }
    }
}