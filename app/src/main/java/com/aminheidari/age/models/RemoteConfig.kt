package com.aminheidari.age.models

import com.aminheidari.age.utils.compareVersionTo
import com.squareup.moshi.Json

data class RemoteConfig(
    @Json(name = "store_url") val storeUrl: String,
    val version: Version,
    @Json(name = "age_specs") val ageSpecs: AgeSpecs
) {

    data class Version(
        val minimum: String,
        val latest: String
    ) {

        enum class CompareResult {

            LatestVersion,
            OptionalUpgrade,
            ForcedUpgrade
        }

        fun compare(appVersion: String): CompareResult {
            return if (appVersion.compareVersionTo(minimum) < 0) {
                CompareResult.ForcedUpgrade
            } else if (appVersion.compareVersionTo(latest) < 0) {
                CompareResult.OptionalUpgrade
            } else {
                CompareResult.LatestVersion
            }
        }

    }

    data class AgeSpecs(
        // Default age shown when launching the app on the date picker, in terms of years.
        @Json(name = "default_age") val defaultAge: Int,
        // Max allowable age (to be picked initially), in terms of years.
        @Json(name = "max_age") val maxAge: Int
    )

}