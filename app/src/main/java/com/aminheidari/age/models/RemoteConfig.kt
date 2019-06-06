package com.aminheidari.age.models

import com.aminheidari.age.utils.compareVersionTo
import com.squareup.moshi.Json
import kotlin.math.min

data class RemoteConfig(
    @Json(name = "store_url") val storeUrl: String,
    val version: Version
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

}