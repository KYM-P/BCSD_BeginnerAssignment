package com.example.week10

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week10.databinding.ActivityItemBinding

class RAdapter (
    private val items: ArrayList<ListData>,
    private val clickListener: (ListData) -> Unit
) : RecyclerView.Adapter<RAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ActivityItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var item = binding.llItem
        var itemName = binding.tvItemName
        var itemArtist = binding.tvItemArtist
        var itemAlbum = binding.tvItemAlbum
        var itemUri = binding.tvItemUri
        var itemTime = binding.tvItemTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // viewHolder 생성마다 호출 / 데이터 바인딩 전 상태
        var binding = ActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // 데이터 연결시 호출 / 데이터 바인딩 이후 상태
        holder.apply {
            itemName.text = items[position].name
            itemName.isSelected = true // marquee 효과
            itemArtist.text = items[position].artist
            itemArtist.isSelected = true
            itemAlbum.text = items[position].album
            itemAlbum.isSelected = true
            itemUri.text = items[position].uri.toString()
            itemUri.isSelected = true
            val time_H = items[position].duration / (3600 * 1000)
            val time_M = (items[position].duration - (time_H * 3600 * 1000)) / (60 * 1000)
            val time_S = (items[position].duration - (time_H * 3600 * 1000) - (time_M * 60 * 1000)) / (1 * 1000)
            itemTime.text = "시간 : $time_H : $time_M : $time_S"
            item.setOnClickListener{
                clickListener(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}