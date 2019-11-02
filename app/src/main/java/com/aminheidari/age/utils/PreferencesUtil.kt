package com.aminheidari.age.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.aminheidari.age.App
import com.aminheidari.age.config.RemoteConfigManager
import com.aminheidari.age.constants.Constants
import com.aminheidari.age.models.AppWidgetOverride
import com.aminheidari.age.models.Birthday
import com.aminheidari.age.receivers.AgeUpdateReceiver
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*

class PreferencesUtil {

    companion object {

        // ====================================================================================================
        // region Constants/Types
        // ====================================================================================================

        private enum class Key {
            DefaultBirthday,
            CachedRemoteConfig,
            SkippedLatestVersion,
            AppWidgetOverride,
            MultipleAgesPurchaseToken,
            CachedMonitorRate // Hash for `MultipleAgesPurchaseToken`
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

        var defaultBirthday: Birthday?
            get() {
                val stringValue = sharedPreferences.getString(Key.DefaultBirthday.name, null)
                return if (stringValue != null) {
                    return moshi.adapter(Birthday::class.java).fromJson(stringValue)
                } else {
                    null
                }
            }
            set(value) {
                if (value == null) {
                    editor.remove(Key.DefaultBirthday.name).apply()
                } else {
                    editor.putString(Key.DefaultBirthday.name, moshi.adapter(Birthday::class.java).toJson(value)).apply()
                }

                // Send an update to `AgeUpdateReceiver` to update any existing `AgeAppWidget` instances.
                App.instance.sendBroadcast(Intent(App.instance.applicationContext, AgeUpdateReceiver::class.java))
            }

        /**
         * To be accessed only by the private computed property `cachedRemoteConfig`, of `RemoteConfigManager`.
         */
        var cachedRemoteConfig: RemoteConfigManager.CachedRemoteConfig?
            get() {
                val stringValue = sharedPreferences.getString(Key.CachedRemoteConfig.name, null)
                return if (stringValue != null) {
                    return moshi.adapter(RemoteConfigManager.CachedRemoteConfig::class.java).fromJson(stringValue)
                } else {
                    null
                }
            }
            set(value) {
                if (value == null) {
                    editor.remove(Key.CachedRemoteConfig.name).apply()
                } else {
                    editor.putString(Key.CachedRemoteConfig.name, moshi.adapter(RemoteConfigManager.CachedRemoteConfig::class.java).toJson(value)).apply()
                }
            }

        /**
         * The latestVersion which has been skipped at the moment.
         * For example, user may have skipped upgrade to the latestVersion of 2.2.0 (and `2.2.0` will be persisted here),
         * however, when the latestVersion on the store turns out to be `2.3.0`,
         * then we'll know that we haven't presented that to the user yet.
         */
        var skippedLatestVersion: String?
            get() = sharedPreferences.getString(Key.SkippedLatestVersion.name, null)
            set(value) {
                editor.putString(Key.SkippedLatestVersion.name, value).apply()
            }

        /**
         * Whether or not the containing app wants the override the app widget.
         */
        var appWidgetOverride: AppWidgetOverride
            get() {
                val stringValue = sharedPreferences.getString(Key.AppWidgetOverride.name, null)
                return if (stringValue != null) {
                    return stringValue.deserializeObject() as AppWidgetOverride
                } else {
                    AppWidgetOverride.None
                }
            }
            set(value) {
                editor.putString(Key.AppWidgetOverride.name, value.serializeToString()).apply()

                // Send an update to `AgeUpdateReceiver` to update any existing `AgeAppWidget` instances.
                App.instance.sendBroadcast(Intent(App.instance.applicationContext, AgeUpdateReceiver::class.java))
            }

        var multipleAgesPurchaseToken: String?
            get() = getProtectedString(Companion.Key.MultipleAgesPurchaseToken, Companion.Key.CachedMonitorRate, null)
            set(value) {
                setProtectedString(Companion.Key.MultipleAgesPurchaseToken, Companion.Key.CachedMonitorRate, value)
            }

        // endregion

        // ====================================================================================================
        // region Methods
        // ====================================================================================================

        private fun getProtectedString(originalKey: Key, hashKey: Key, defaultValue: String?): String? {
            val value = sharedPreferences.getString(originalKey.name, null)
            return if (value != null) {
                if (saltHash(value) == sharedPreferences.getString(hashKey.name, null)) {
                    value
                } else {
                    editor.putString(null, originalKey.name).apply()
                    editor.putString(null, hashKey.name).apply()
                    null
                }
            } else {
                null
            }
        }

        private fun setProtectedString(originalKey: Key, hashKey: Key, value: String?) {
            if (value != null) {
                // Persist the data and its hash.
                editor.putString(value, originalKey.name).apply()
                editor.putString(saltHash(value), hashKey.name).apply()
            } else {
                editor.putString(null, originalKey.name).apply()
                editor.putString(null, hashKey.name).apply()
            }
        }

        private fun saltHash(string: String): String {
            return String.format("%s%s", string, Constants.DeviceIntegrity.hashSaltString).toByteArray().sha512.raw
        }

        // endregion

    }

}