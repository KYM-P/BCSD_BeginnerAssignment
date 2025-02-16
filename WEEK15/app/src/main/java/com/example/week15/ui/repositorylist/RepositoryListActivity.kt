package com.example.week15.ui.repositorylist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.week15.BuildConfig
import com.example.week15.R
import com.example.week15.databinding.ActivityRepositoryListBinding
import com.example.week15.ui.main.MainActivity.Companion.NULL_NAME
import com.example.week15.ui.repositorylist.recyclerview.adapter.RepositoryListRecyclerAdapter

class RepositoryListActivity : AppCompatActivity() {

    companion object {
        const val SORT = "created"
        const val PER_PAGE = 10
        const val GITHUB_TOKEN = BuildConfig.GITHUB_TOKEN
    }

    private lateinit var binding: ActivityRepositoryListBinding

    private lateinit var viewModel : RepositoryListActivityModel

    private lateinit var adapter: RepositoryListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRepositoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username") ?: NULL_NAME

        if(username == NULL_NAME) {
            supportActionBar?.title = getString(R.string.repo_error_username)
        }else {
            supportActionBar?.title = username

            viewModel = RepositoryListActivityModel(username)
            binding.viewModel = viewModel

            viewModel.isLoading.observe(this, Observer { isLoading ->
                if(isLoading) {
                    binding.repositorylistProbar.visibility = View.VISIBLE
                }else {
                    binding.repositorylistProbar.visibility = View.GONE
                    Log.e("MY_TAG","detected")
                    repositoryListRecyclerAdapterUpdate()
                }
            })

            adapter = RepositoryListRecyclerAdapter(mutableListOf(), this)
            binding.repositorylistRcy.adapter = adapter
            binding.repositorylistRcy.addItemDecoration(DividerItemDecoration(this, 1))
            binding.repositorylistRcy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if(!binding.repositorylistRcy.canScrollVertically(1) &&
                        !(viewModel.isLoading.value ?: false) &&
                        !viewModel.isFullLoad) {
                        Log.e("MY_TAG","추가 로딩")
                        viewModel.isLoading.value = true
                        viewModel.searchMoreRepo()
                    }
                }
            })
        }
    }

    fun repositoryListRecyclerAdapterUpdate() {
        adapter.update(viewModel.searchRepoList)
    }

    fun intentWebPage(view : View) {
        val position = binding.repositorylistRcy.getChildAdapterPosition(view)
        val url = viewModel.searchRepoList[position].html_url
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}