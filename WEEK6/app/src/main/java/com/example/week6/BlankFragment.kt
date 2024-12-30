package com.example.week6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.setFragmentResult
import com.example.week6.databinding.FragmentBlankBinding
import kotlin.random.Random


class BlankFragment : Fragment() {
    lateinit var binding: FragmentBlankBinding
    var rand_max_num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rand_max_num = arguments?.getInt("RandNumber")?: 0
        rand_max_num = Random.nextInt(0,rand_max_num + 1)
    }
    override fun onCreateView(
        // view > Button, TextView ... 등 / viewGroup > LinearLayout, ConstraintLayout ... 등
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_blank, container, false)
        binding = FragmentBlankBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // Fragment 관련 작업하기 알맞은 장소
        super.onViewCreated(view, savedInstanceState)
        binding.tvFragmentValue.text = rand_max_num.toString()
        val callBack = object : OnBackPressedCallback (true) {
            override fun handleOnBackPressed() {
            }
        }
        // Result 추가
        setFragmentResult("RandBundle", Bundle().apply { putInt("ResultNumber", rand_max_num) })
        //requireActivity().onBackPressedDispatcher.addCallback(callBack) // callBack 함수를 선언해 넘겨줄 수 있음
        requireActivity().onBackPressedDispatcher.addCallback { requireActivity().supportFragmentManager.popBackStack() }
    }
}