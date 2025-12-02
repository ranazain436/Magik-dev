package com.topjohnwu.magisk.core.model.module

import com.google.gson.annotations.SerializedName

data class GithubRepo(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("description")
    val description: String,
    var version: String = "",
    var versionCode: Int = 0,
    var author: String = ""
) {
    data class Owner(
        @SerializedName("login")
        val login: String
    )
}
