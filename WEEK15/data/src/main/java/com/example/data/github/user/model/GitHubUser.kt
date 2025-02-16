package com.example.data.github.user.model

import com.google.gson.annotations.SerializedName

data class GitHubUser (
    @SerializedName("login")
    val userName : String
){
}