package com.example.week14.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.word.model.WordItem
import com.example.week14.databinding.ActivityWordItemBinding
import com.example.week14.ui.main.MainActivity

class WordsRecyclerAdapter (
    private val items : ArrayList<WordItem>,
    val context : MainActivity
) : RecyclerView.Adapter<WordsRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ActivityWordItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // viewHolder 생성마다 호출 / 데이터 바인딩 전 상태
        var binding = ActivityWordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // 데이터 연결시 호출 / 데이터 바인딩 이후 상태
        holder.apply {
            binding.wordNameTv.text = items[position].wordName
            binding.wordMeanTv.text = items[position].wordMean
            binding.wordImgIv.setImageURI(items[position].wordImg) // glide 비사용
            binding.wordItemLy.setOnClickListener {
                context.apply { detectSelectedWord(binding.wordItemLy) }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}