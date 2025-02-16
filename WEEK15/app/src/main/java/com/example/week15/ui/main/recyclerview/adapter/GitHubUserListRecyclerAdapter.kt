package com.example.week15.ui.main.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.data.github.user.model.GitHubUser
import com.example.week15.databinding.ActivityMainListItemBinding
import com.example.week15.ui.main.MainActivity
import com.example.week15.ui.main.recyclerview.adapter.diffutil.GitHubUserListDiffUtil

class GitHubUserListRecyclerAdapter (
    private var items : List<GitHubUser>,
    val context : MainActivity
) : RecyclerView.Adapter<GitHubUserListRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ActivityMainListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ActivityMainListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.mainItemTitleTv.text = items[position].userName
            binding.mainItem.setOnClickListener {
                context.apply {
                    intentRepositoryList(binding.mainItem)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(newItems: List<GitHubUser>) {
        val diffUtil = GitHubUserListDiffUtil(items, newItems)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtil)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }
}