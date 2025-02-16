package com.example.week15.ui.repositorylist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.github.GitHubApiClient
import com.example.data.github.repo.model.GitHubRepo
import com.example.week15.ui.repositorylist.RepositoryListActivity.Companion.GITHUB_TOKEN
import com.example.week15.ui.repositorylist.RepositoryListActivity.Companion.PER_PAGE
import com.example.week15.ui.repositorylist.RepositoryListActivity.Companion.SORT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryListActivityModel (
    private val username : String
) : ViewModel() {
    val searchRepoList : MutableList<GitHubRepo> = mutableListOf()
    private var recentLastPage = 1
    var isLoading : MutableLiveData<Boolean> = MutableLiveData(true)
    var isFullLoad : Boolean = false

    init {
        viewModelScope.launch {
            delay(300)
            searchGitHubRepo(username)
        }
    }

    fun searchGitHubRepo(username : String, sort : String = SORT, perPage: Int = PER_PAGE, page: Int = recentLastPage) {
        GitHubApiClient.apiService.searchUserRepos(username, "Bearer ${GITHUB_TOKEN}" , sort, perPage, page).enqueue(object :
            Callback<List<GitHubRepo>> {
            override fun onResponse(call: Call<List<GitHubRepo>>, response: Response<List<GitHubRepo>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if(it.size != 0) {
                            searchRepoList.addAll(it)
                            Log.e("MY_TAG","Laod items ${response.body()?.size} , All Items ${searchRepoList.size}")
                        }
                        else {
                            isFullLoad = true
                        }
                    }
                } else {
                    Log.e("MY_TAG","Error: ${response.code()}")
                }
                isLoading.value = false
            }

            override fun onFailure(call: Call<List<GitHubRepo>>, t: Throwable) {
                Log.e("MY_TAG","Failure: ${t.message}")
            }
        })
    }

    fun searchMoreRepo() {
        viewModelScope.launch {
            delay(300)
            ++recentLastPage
            searchGitHubRepo(username)
        }
    }
}