package com.example.week12.viewpager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week12.R
import com.example.week12.databinding.FragmentAlphabetViewBinding

class AlphabetViewFragment(
    val alphabetValue : String,
) : Fragment() {
    lateinit var binding: FragmentAlphabetViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_alphabet_view, container, false)
        binding = FragmentAlphabetViewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.alphabetMain.text = savedInstanceState?.getString("setAlphabetValue")
        binding.alphabetMain.text = alphabetValue
    }
}