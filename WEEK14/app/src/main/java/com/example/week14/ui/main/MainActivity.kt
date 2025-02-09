package com.example.week14.ui.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.example.domain.word.model.WordItem
import com.example.week14.ui.main.adapter.WordsRecyclerAdapter
import com.example.week14.databinding.ActivityMainBinding
import com.example.week14.ui.add.AddWordItemActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        const val RESULT_ADD = 0
        const val RESULT_REVISE = 1
        const val RESULT_EMPTY = -1
    }

    private lateinit var binding : ActivityMainBinding // Activity 의 root 가 layout 일 때 작동
    private val viewModel : MainActivityModel by viewModels()

    // resultLauncher for addActivity
    val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val newWord = WordItem(
            result.data?.getStringExtra("newWordName")?:"",
            result.data?.getStringExtra("newWordMean")?:"",
            Uri.parse(result.data?.getStringExtra("newWordImg")?:"")
        )
        if(viewModel != null) {
            when(result.resultCode) {
                RESULT_ADD -> {
                    viewModel.addWord(newWord)
                    binding.wordsRcv.adapter?.notifyItemInserted(viewModel.wordsListSize() - 1)
                }
                RESULT_REVISE -> {
                    viewModel.updateSelectedWord(newWord)
                    binding.wordsRcv.adapter?.notifyItemChanged(viewModel.selectedWordPosition)
                }
                else -> {
                    Toast.makeText(this, "resultCodeError", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // viewModel (hilt 로 대체)
        binding.viewModel = viewModel
        // activity
        binding.activity = this

        // liveData
        viewModel.selectedWordName.observe(this) {str ->
            binding.selectedWordNameTv.text = str
        }
        viewModel.selectedWordMean.observe(this) {str ->
            binding.selectedWordMeanTv.text = str
        }

        // set Observer for setting recyclerView
        binding.viewModel?.isDataLoaded?.observe(this, Observer { isDataLoaded ->
            if(isDataLoaded) {
                // set recyclerView
                val adapter = WordsRecyclerAdapter(viewModel.wordsList , this)
                binding.wordsRcv.adapter = adapter
                binding.wordsRcv.addItemDecoration(DividerItemDecoration(this, 1))
                Toast.makeText(this,"loaded : ${viewModel.wordsList.size} items",Toast.LENGTH_SHORT).show()
            }
        })
        // set recyclerView
        binding.viewModel?.setItemsFromDb()
    }

    // detectSelectedWord */ recyclerView Item Click
    fun detectSelectedWord(view: View) {
        var position = binding.wordsRcv.getChildAdapterPosition(view)
        viewModel.updateSelectedData(viewModel.getWord(position).wordName,
            viewModel.getWord(position).wordMean,
            viewModel.getWord(position).wordImg,
            position
        )
        Glide.with(this)
            .load(viewModel.selectedWordImg.value)
            .into(binding.selectedWordImgIv)
    }

    // addWordBtn
    fun onClickIntentAddWordItemActivity(view : View) {
        val intent = Intent(this, AddWordItemActivity::class.java).apply {
            putExtra("resultCode",RESULT_ADD)
        }
        resultLauncher.launch(intent)
    }

    // reviseWordBtn
    fun onClickReviseWord(view : View) {
        if(viewModel.selectedWordPosition == -1) {
            Toast.makeText(this, "emptyWord", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, AddWordItemActivity::class.java).apply {
            putExtra("resultCode", RESULT_REVISE)
            putExtra("oldWordName", viewModel.selectedWordName.value)
            putExtra("oldWordMean", viewModel.selectedWordMean.value)
            putExtra("oldWordImg", viewModel.selectedWordImg.value.toString())
        }
        resultLauncher.launch(intent)
    }

    // deleteWordBtn
    fun onClickDeleteWord(view : View) {
        val selectedPosition = viewModel.selectedWordPosition
        viewModel.removeSelectedWord()
        binding.wordsRcv.adapter?.notifyItemRemoved(selectedPosition)
    }
}