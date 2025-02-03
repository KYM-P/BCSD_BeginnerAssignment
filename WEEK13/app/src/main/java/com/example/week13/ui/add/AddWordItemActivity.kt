package com.example.week13.ui.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.week13.databinding.ActivityAddWordItemBinding
import com.example.week13.ui.main.MainActivity.Companion.RESULT_EMPTY
import com.example.week13.ui.main.MainActivity.Companion.RESULT_REVISE

class AddWordItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddWordItemBinding // Activity 의 root 가 layout 일 때 작동
    val addModel = AddWordItemActivityModel("","")
    private var resultCode : Int = RESULT_EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWordItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // viewModel
        binding.viewModel = addModel
        // set view
        resultCode = intent.getIntExtra("resultCode", RESULT_EMPTY)
        when (resultCode) {
            RESULT_REVISE -> {
                updateAddModel(intent.getStringExtra("oldWordName")?:"",
                    intent.getStringExtra("oldWordMean")?:""
                )
            }
        }
        // btn
        binding.wordAddBtn.setOnClickListener {
            val intent = Intent().apply {
                putExtra("newWordName", binding.wordNameEt.text.toString())
                putExtra("newWordMean", binding.wordMeanEt.text.toString())
            }
            setResult(resultCode, intent)
            finish()
        }
    }
    private fun updateAddModel(name : String, mean : String){
        addModel.newWordName = name
        addModel.newWordMean = mean
        binding.invalidateAll()
    }
}