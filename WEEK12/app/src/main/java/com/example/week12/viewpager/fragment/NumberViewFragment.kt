package com.example.week12.viewpager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week12.R
import com.example.week12.databinding.FragmentNumberViewBinding

class NumberViewFragment (
    val numberValue : String
) : Fragment() {
    lateinit var binding: FragmentNumberViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_number_view, container, false)
        binding = FragmentNumberViewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.numberMain.text = savedInstanceState?.getString("setNumberValue")
        binding.numberMain.text = numberValue

    }
}