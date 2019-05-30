package com.aminheidari.age.models

import com.squareup.moshi.Json

data class RemoteConfig(
    @Json(name = "store_url") val storeUrl: String,
    val version: Version
) {

    data class Version(
        val minimum: String,
        val latest: String
    )

}