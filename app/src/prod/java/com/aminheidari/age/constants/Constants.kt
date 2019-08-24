package com.aminheidari.age.constants

object Constants {

    object RemoteConfig {

        object Certificate {
            const val domain = "dev.api.config.aminheidari.com"
            const val signature = "sha256/TezTej37BLoMWk01uVKp18WE2XRf+K6F1iPCbIvRBVM="
        }

        // URL for the remote config.
        const val url = "https://api.config.aminheidari.com/projects/agepal/ios"

        // Api key.
        const val apiKeyHeaderField = "X-API-Key"
        const val apiKeyHeaderValue = "cCZA3IPS2K6R6tTzaRJrK3wVzinwogXk3A0nim8K"

        // Time interval (in seconds) during which the cache will be used rather than making a new api call.
        // If this is lower than the life time of a single application process, then we'll have a re-fetch of the config on each app launch.
        const val freshCacheTime = 30

        // Time interval (in seconds) after which the cache expires and a fresh remote config MUST be fetched.
        // Note that, since we don't do a re-fetch in the same app session, this must be much longer than an application process lifetime (in the order of hours if not days).
        const val expireTime = 7 * 24 * 3600
    }

    object AgeCalculation {
        // Refresh interval in mili seconds.
        const val refreshInterval: Long = 10
    }

    object DeviceIntegrity {
        // Maximum time the device is allowed to have a time diff from the api (miliseconds).
        const val maxAllowedApiTimeDifference: Long = 10 * 60 * 1000

        const val hashSaltString = "Kfu6myLk2f79cKVA6fOs3xV67c1plcCh"
    }

    object Billing {
        const val multipleAgesId = "multiple_ages"
    }

}