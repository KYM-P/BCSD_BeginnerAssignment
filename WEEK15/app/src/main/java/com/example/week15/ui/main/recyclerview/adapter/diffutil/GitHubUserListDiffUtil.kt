package com.example.week15.ui.main.recyclerview.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.data.github.user.model.GitHubUser

class GitHubUserListDiffUtil (
    private val oldList: List<GitHubUser>,
    private val newList: List<GitHubUser>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.userName == newItem.userName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}