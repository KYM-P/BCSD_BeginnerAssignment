package com.example.week15.ui.repositorylist.recyclerview.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.data.github.repo.model.GitHubRepo

class RepositoryListDiffUtil (
    private val oldList: List<GitHubRepo>,
    private val newList: List<GitHubRepo>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.id == newItem.id &&
                    oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
}