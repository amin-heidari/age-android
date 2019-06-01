package com.aminheidari.age.constants

class Constants {

    class RemoteConfig {

        companion object {

            // URL for the remote config.
            const val url = "https://api.config.aminheidari.com/projects/agepal/ios"

            // Time interval (in seconds) during which the cache will be used rather than making a new api call.
            const val freshCacheTime = 30

            // Time interval (in seconds) after which the cache expires and a fresh remote config MUST be fetched.
            const val expireTime = 7 * 24 * 3600
        }
    }

}