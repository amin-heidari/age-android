package com.aminheidari.age.config

import com.aminheidari.age.constants.Constants
import com.aminheidari.age.models.AppException
import com.aminheidari.age.models.Completion
import com.aminheidari.age.models.Either
import com.aminheidari.age.models.RemoteConfig
import com.aminheidari.age.utils.Logger
import com.aminheidari.age.utils.PreferencesUtil
import com.aminheidari.age.utils.PreferencesUtil.Companion.cachedRemoteConfig
import com.aminheidari.age.utils.addingTimerInterval
import com.aminheidari.age.utils.retrofitCallback
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.lang.Exception
import java.util.*

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
            completion(Either.success(cachedRemoteConfig!!.remoteConfig))
            return null;
        } else {
            // There either isn't a cache, or if there is one, it's not fresh. So try to make the api call.
            val call = apiClient.getRemoteConfig()
            call.enqueue(retrofitCallback { result ->
                when(result) {
                    is Either.failure -> {
                        when (result.exception) {
                            is AppException.connection -> {
                                val cached = cachedRemoteConfig
                                if (cached != null && !cached.isExpired) {
                                    completion(Either.success(cached.remoteConfig))
                                } else {
                                    completion(Either.failure(result.exception))
                                }
                            }
                            else -> {
                                completion(Either.failure(result.exception))
                            }
                        }
                    }
                    is Either.success -> {
                        // Update the cache.
                        cachedRemoteConfig = CachedRemoteConfig(result.data, Calendar.getInstance().time)
                        completion(Either.success(result.data))
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