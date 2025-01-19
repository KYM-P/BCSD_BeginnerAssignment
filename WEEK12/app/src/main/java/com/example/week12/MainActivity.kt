package com.example.week12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.week12.databinding.ActivityMainBinding
import com.example.week12.viewpager.fragment.AlphabetViewFragment
import com.example.week12.viewpager.fragment.ColorViewFragment
import com.example.week12.viewpager.fragment.NumberViewFragment
import com.example.week12.viewpager2.ViewPager2Adapter
import com.example.week12.viewpager2.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Objects

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // list
        val rainbowList = listOf("Red","Orange","Yellow","Green","Blue","Navy","Purple")
        val numberList = List(10) { i -> i }
        val alphaberList = List(26) { i -> 'A' + i}


        // rainbowFragment
        val rainbowFragments = mutableListOf<Fragment>()
        rainbowList.forEach {s : String ->
            rainbowFragments.add(ColorViewFragment(s))
        }
        // numberFragment
        val numberFragments = mutableListOf<Fragment>()
        numberList.forEach { i ->
            numberFragments.add(NumberViewFragment(i.toString()))
        }
        // alphabetFragment
        val alphabetFragments = mutableListOf<Fragment>()
        alphaberList.forEach { c ->
            alphabetFragments.add(AlphabetViewFragment(c.toString()))
        }

        // tabLayout

        // viewPager2
        binding.viewPager2.setPageTransformer(ZoomOutPageTransformer())
        // bottomNavigation
        binding.bottomNavi.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.menu_rainbow -> {
                    setViewPager(rainbowFragments, rainbowList)
                }
                R.id.menu_number -> {
                    setViewPager(numberFragments, numberList)
                }
                R.id.menu_alphabet -> {
                    setViewPager(alphabetFragments, alphaberList)
                }
                else -> {
                    setViewPager(alphabetFragments, alphaberList)
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    fun<T> setViewPager(fragments : MutableList<Fragment>, list : List<T> ) {
        binding.viewPager2.adapter = ViewPager2Adapter(this, fragments)
        TabLayoutMediator(binding.tabLayout,binding.viewPager2) { tab, position ->
            tab.text = list[position].toString()
        }.attach()
    }
}