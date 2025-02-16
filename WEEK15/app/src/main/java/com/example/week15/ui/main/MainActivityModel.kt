package com.example.week15.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.github.GitHubApiClient
import com.example.data.github.user.GitHubUserResponse
import com.example.data.github.user.model.GitHubUser
import com.example.week15.ui.main.MainActivity.Companion.NULL_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivityModel () : ViewModel() {
    val searchUserList : MutableLiveData<List<GitHubUser>> = MutableLiveData(listOf())

    fun searchEtDebounce (flow : Flow<String>) {
        viewModelScope.launch {
            flow
                .debounce(500) // 500ms 동안 입력이 없으면 실행 (aplly 내에서 실행 불가)
                .distinctUntilChanged()
                .collect { text ->
                    Log.e("MY_TAG","search : ${text}")
                    searchGitHubUser(text)
                }
        }
    }

    fun searchGitHubUser(query : String) {
        GitHubApiClient.apiService.searchUsers(query).enqueue(object : Callback<GitHubUserResponse> {
            override fun onResponse(call: Call<GitHubUserResponse>, response: Response<GitHubUserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.users?.let { searchUserList.value = it }
                } else {
                    Log.e("MY_TAG","Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GitHubUserResponse>, t: Throwable) {
                Log.e("MY_TAG","Failure: ${t.message}")
            }
        })
    }

    fun getUserNameAt(index : Int) : String {
        return searchUserList.value?.get(index)?.userName ?: NULL_NAME
    }
}