package com.example.week15.ui.repositorylist.recyclerview.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.github.repo.model.GitHubRepo
import com.example.week15.databinding.ActivityRepositoryListLitemBinding
import com.example.week15.ui.repositorylist.RepositoryListActivity
import com.example.week15.ui.repositorylist.recyclerview.adapter.diffutil.RepositoryListDiffUtil

class RepositoryListRecyclerAdapter (
    private var items : MutableList<GitHubRepo>,
    val context : RepositoryListActivity
    ) : RecyclerView.Adapter<RepositoryListRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ActivityRepositoryListLitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    private val imageUrls = listOf(
        "https://github.com/fluidicon.png",
        "https://github.githubassets.com/images/icons/emoji/unicode/2b50.png",
        "https://github.githubassets.com/images/icons/emoji/unicode/1f374.png"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ActivityRepositoryListLitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.itemTitleTv.text = items[position].name
            binding.itemDetailTv.text = items[position].description
            binding.itemStarTv.text = items[position].stargazers_count.toString()
            binding.itemForkTv.text = items[position].forks_count.toString()
            imageLoad(imageUrls[0],binding.itemIconIv)
            imageLoad(imageUrls[1],binding.itemStarIv)
            imageLoad(imageUrls[2],binding.itemForkIv)
            binding.item.setOnClickListener {
                context.apply {
                    intentWebPage(binding.item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(newItems: MutableList<GitHubRepo>) {
        Log.e("MY_TAG","DIFF ${items.size}, ${newItems.size}")
        val diffUtil = RepositoryListDiffUtil(items, newItems)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtil)
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun imageLoad(url: String, iv : ImageView) {
        Glide.with(context)
            .load(url)
            .into(iv)
    }
}