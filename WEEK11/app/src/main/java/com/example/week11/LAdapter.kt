package com.example.week11

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.week11.databinding.ItemBinding
import java.util.ArrayList;

class LAdapter (
    val items: ArrayList<ListData>
) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        val data = items[position]
        binding.tvItemName.text = data.name
        binding.tvItemTime.text = data.time
        return binding.root
    }
}
