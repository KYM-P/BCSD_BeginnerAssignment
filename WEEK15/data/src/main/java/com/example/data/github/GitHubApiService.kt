package com.example.data.github

import com.example.data.github.repo.model.GitHubRepo
import com.example.data.github.user.GitHubUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {

    @GET("search/users")
    fun searchUsers(
        @Query("q") query: String
    ): Call<GitHubUserResponse>

    @GET("users/{username}/repos")
    fun searchUserRepos(
        @Path("username") username: String,
        @Header("Authorization") authorization : String,
        @Query("sort") sort: String = "created", // 기본 정렬 방식 (최신순)
        @Query("per_page") perPage: Int = 30, // 한 페이지당 저장소 개수
        @Query("page") page: Int = 1 // 페이지 번호
    ) : Call<List<GitHubRepo>>
}