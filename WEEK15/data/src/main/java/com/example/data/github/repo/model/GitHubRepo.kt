package com.example.data.github.repo.model

data class GitHubRepo (
    val id : Int,
    val name : String, // 이름이 동일하므로 SerializedName 생략 가능
    val html_url : String,
    val description : String,
    val owner : Owner,
    val stargazers_count : Int,
    val forks_count : Int
) {
}

data class Owner(
    val login: String,
    val avatar_url: String
)