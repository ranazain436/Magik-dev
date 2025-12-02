package com.topjohnwu.magisk.core.model

import com.google.gson.annotations.SerializedName

data class Release(
    @SerializedName("zipball_url")
    val zipballUrl: String
)
