package com.topjohnwu.magisk.core.model

import com.google.gson.annotations.SerializedName

data class RepoDetails(
    @SerializedName("default_branch")
    val defaultBranch: String
)
