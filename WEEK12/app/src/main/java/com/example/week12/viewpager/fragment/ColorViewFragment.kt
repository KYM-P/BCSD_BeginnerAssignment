package com.example.week12.viewpager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.week12.R
import com.example.week12.databinding.FragmentColorViewBinding

class ColorViewFragment (
    val colorValue : String,
) : Fragment() {
    lateinit var binding: FragmentColorViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_color_view, container, false)
        binding = FragmentColorViewBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val colorValue = savedInstanceState?.getString("setColorValue")?: "Null"
        val resultColorCode = when (colorValue) {
            "Red" -> {
                R.color.red
            }
            "Orange" -> {
                R.color.orange
            }
            "Yellow" -> {
                R.color.yellow
            }
            "Green" -> {
                R.color.green
            }
            "Blue" -> {
                R.color.blue
            }
            "Navy" -> {
                R.color.navy
            }
            "Purple" -> {
                R.color.purple
            }
            else -> {
                R.color.my_d_gray
            }
        }
        binding.colorMain.setBackgroundColor(ContextCompat.getColor(requireContext(),resultColorCode)) // context 가 null 아님을 보장
    }
}