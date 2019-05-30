package com.aminheidari.age.constants

object Constants {

    object RemoteConfig {

            object Certificate {
                const val domain = "dev.api.config.aminheidari.com"
                const val signature = "sha256/TezTej37BLoMWk01uVKp18WE2XRf+K6F1iPCbIvRBVM="
            }

            // URL for the remote config.
            const val url = "https://dev.api.config.aminheidari.com/projects/agepal/ios/"

            // Time interval (in seconds) during which the cache will be used rather than making a new api call.
            const val freshCacheTime = 30

            // Time interval (in seconds) after which the cache expires and a fresh remote config MUST be fetched.
            const val expireTime = 7 * 24 * 3600
    }

}