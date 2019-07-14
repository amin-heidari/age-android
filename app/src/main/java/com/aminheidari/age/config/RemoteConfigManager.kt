package com.aminheidari.age.config

import com.aminheidari.age.constants.Constants
import com.aminheidari.age.models.AppException
import com.aminheidari.age.utils.Completion
import com.aminheidari.age.models.RemoteConfig
import com.aminheidari.age.utils.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import java.util.*
import kotlin.math.abs

object RemoteConfigManager {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    data class CachedRemoteConfig(
        val remoteConfig: RemoteConfig,
        val cachedTime: Date
    ) {
        val isFresh: Boolean
            get() = cachedTime > Calendar.getInstance().time.addingTimerInterval(-Constants.RemoteConfig.freshCacheTime)

        val isExpired: Boolean
            get() = cachedTime < Calendar.getInstance().time.addingTimerInterval(-Constants.RemoteConfig.expireTime)
    }

    interface APIClient {
        @Headers("${Constants.RemoteConfig.apiKeyHeaderField}: ${Constants.RemoteConfig.apiKeyHeaderValue}")
        @GET(".")
        fun getRemoteConfig(): Call<RemoteConfig>
    }

    // endregion

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region API
    // ====================================================================================================

    fun fetchConfig(completion: Completion<RemoteConfig>): Call<RemoteConfig>? {
        // Check if there's a fresh copy of the remote config cached.
        if (cachedRemoteConfig != null && cachedRemoteConfig!!.isFresh) {
            completion(Either.Success(cachedRemoteConfig!!.remoteConfig))
            return null
        } else {
            // There either isn't a cache, or if there is one, it's not fresh. So try to make the api call.
            val call = apiClient.getRemoteConfig()
            call.enqueue(retrofitFullCallback { result ->
                when(result) {
                    is Either.Failure -> {
                        when (result.exception) {
                            is AppException.Connection -> {
                                val cached = cachedRemoteConfig
                                if (cached != null && !cached.isExpired) {
                                    completion(Either.Success(cached.remoteConfig))
                                } else {
                                    completion(Either.Failure(result.exception))
                                }
                            }
                            else -> {
                                completion(Either.Failure(result.exception))
                            }
                        }
                    }
                    is Either.Success -> {
                        // Success block to execute.
                        val completeSuccess = {
                            // Update the cache.
                            cachedRemoteConfig = CachedRemoteConfig(result.data.data, Calendar.getInstance().time)
                            completion(Either.Success(result.data.data))
                        }

                        // Check if the response has a header named `Date`,
                        // and use it to make sure the server time and client time are in sync (and throw an error otherwise).
                        val apiDate = result.data.response.headers().getDate("date")
                        if (apiDate != null) {
                            // There is a date coming back from the api, use to to validate the integrity of the time on the device.
                            if (abs(Calendar.getInstance().time.time - apiDate.time) > Constants.DeviceIntegrity.maxAllowedApiTimeDifference) {
                                completion(Either.Failure(AppException.IncorrectDeviceDateTime))
                            } else {
                                completeSuccess()
                            }
                        } else {
                            completeSuccess()
                        }
                    }
                }
            })
            return call
        }
    }


    val remoteConfig: RemoteConfig
        get() {
            if (cachedRemoteConfig == null) { Logger.wtf("Not supported") }
            return cachedRemoteConfig!!.remoteConfig
        }


    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
    }

    private val apiClient: APIClient by lazy {
        val client = OkHttpClient.Builder()
            .certificatePinner(
                CertificatePinner.Builder()
                    .add(Constants.RemoteConfig.Certificate.domain, Constants.RemoteConfig.Certificate.signature)
                    .build()
            )
            .build()

        Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.RemoteConfig.url)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(APIClient::class.java)
    }

    /**
     * This computed property is the only place in the app wherein `PreferencesUtil.cachedRemoteConfig` is used.
     */
    private var cachedRemoteConfig: CachedRemoteConfig?
        get() {
            val cachedRemoteConfig = PreferencesUtil.cachedRemoteConfig
            return if (cachedRemoteConfig != null) {
                if (cachedRemoteConfig.isExpired) {
                    PreferencesUtil.cachedRemoteConfig = null
                    null
                } else {
                    cachedRemoteConfig
                }
            } else {
                null
            }
        }
        set(value) {
            PreferencesUtil.cachedRemoteConfig = value
        }

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    // endregion

}