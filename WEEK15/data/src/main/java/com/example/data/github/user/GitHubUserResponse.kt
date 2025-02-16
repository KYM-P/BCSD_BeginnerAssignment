package com.example.data.github.user

import com.example.data.github.user.model.GitHubUser
import com.google.gson.annotations.SerializedName

data class GitHubUserResponse (
    @SerializedName("items")
    val users: List<GitHubUser>,
) {
}