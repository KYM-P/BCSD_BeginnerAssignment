package com.example.week15.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.week15.databinding.ActivityMainBinding
import com.example.week15.ui.main.recyclerview.adapter.GitHubUserListRecyclerAdapter
import com.example.week15.ui.repositorylist.RepositoryListActivity

import kotlinx.coroutines.flow.*

import com.google.android.material.internal.TextWatcherAdapter
import kotlinx.coroutines.channels.awaitClose

class MainActivity : AppCompatActivity() {
    companion object {
        const val NULL_NAME = ""
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel : MainActivityModel

    private lateinit var adapter: GitHubUserListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = MainActivityModel()
        binding.viewModel = viewModel

        viewModel.searchUserList.observe(this, Observer {
            gitHubUserListRecyclerAdapterUpdate()
        })

        // editText / Debounce : RxJava, Coroutines 의 Flow 등 여러 방법 존재
        viewModel.searchEtDebounce(binding.mainSearchEt.textChangedFlow())

        adapter = GitHubUserListRecyclerAdapter(viewModel.searchUserList.value ?: listOf(), this)
        binding.mainSearchRcy.adapter = adapter
        binding.mainSearchRcy.addItemDecoration(DividerItemDecoration(this, 1))
    }

    fun EditText.textChangedFlow(): Flow<String> { // edittext 에서 flow 반환
        return callbackFlow<String> {
            val watcher = @SuppressLint("RestrictedApi") object : TextWatcherAdapter() {
                override fun afterTextChanged(p0: Editable) {
                    trySend(p0.toString())
                }
            }
            addTextChangedListener(watcher)
            awaitClose { removeTextChangedListener(watcher) // 자동 리스너 제거 (누수 방지)
            }
        }.conflate() // 중간 데이터 삭제 (최신 데이터만 유지)
    }

    fun gitHubUserListRecyclerAdapterUpdate() {
        adapter.update(viewModel.searchUserList.value ?: listOf())
    }

    fun intentRepositoryList(view : View) {
        val position = binding.mainSearchRcy.getChildAdapterPosition(view)
        val intent = Intent(this, RepositoryListActivity::class.java)
            .putExtra("username", viewModel.getUserNameAt(position))
        startActivity(intent)
    }
}