package com.aminheidari.age.utils

import android.content.Context
import android.content.SharedPreferences
import com.aminheidari.age.App
import com.aminheidari.age.config.RemoteConfigManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*

class PreferencesUtil {

    companion object {

        // ====================================================================================================
        // region Constants/Types
        // ====================================================================================================

        private enum class Keys {
            defaultBirthday,
            cachedRemoteConfig
        }

        private val sharedPreferences: SharedPreferences by lazy { App.instance.applicationContext.getSharedPreferences("com.aminheidari.age.prefs", Context.MODE_PRIVATE) }

        private val editor: SharedPreferences.Editor
            get() = sharedPreferences.edit()

        private val moshi: Moshi by lazy {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                .build()
        }

        // endregion

        // ====================================================================================================
        // region API
        // ====================================================================================================

        /**
         * To be accessed only by the private computed property `cachedRemoteConfig`, of `RemoteConfigManager`.
         */
        var cachedRemoteConfig: RemoteConfigManager.CachedRemoteConfig?
            get() {
                val stringValue = sharedPreferences.getString(Keys.cachedRemoteConfig.name, null)
                return if (stringValue != null) {
                    return moshi.adapter(RemoteConfigManager.CachedRemoteConfig::class.java).fromJson(stringValue)
                } else {
                    null
                }
            }
            set(value) {
                if (value == null) {
                    editor.remove(Keys.cachedRemoteConfig.name).apply()
                } else {
                    editor.putString(Keys.cachedRemoteConfig.name, moshi.adapter(RemoteConfigManager.CachedRemoteConfig::class.java).toJson(value)).apply()
                }
            }

        // endregion

    }

}