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
            DefaultBirthday,
            CachedRemoteConfig,
            SkippedLatestVersion
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
                val stringValue = sharedPreferences.getString(Keys.CachedRemoteConfig.name, null)
                return if (stringValue != null) {
                    return moshi.adapter(RemoteConfigManager.CachedRemoteConfig::class.java).fromJson(stringValue)
                } else {
                    null
                }
            }
            set(value) {
                if (value == null) {
                    editor.remove(Keys.CachedRemoteConfig.name).apply()
                } else {
                    editor.putString(Keys.CachedRemoteConfig.name, moshi.adapter(RemoteConfigManager.CachedRemoteConfig::class.java).toJson(value)).apply()
                }
            }

        /**
         * The latestVersion which has been skipped at the moment.
         * For example, user may have skipped upgrade to the latestVersion of 2.2.0 (and `2.2.0` will be persisted here),
         * however, when the latestVersion on the store turns out to be `2.3.0`,
         * then we'll know that we haven't presented that to the user yet.
         */
        var skippedLatestVersion: String?
            get() = sharedPreferences.getString(Keys.SkippedLatestVersion.name, null)
            set(value) {
                editor.putString(Keys.SkippedLatestVersion.name, value)
            }

        // endregion

    }

}