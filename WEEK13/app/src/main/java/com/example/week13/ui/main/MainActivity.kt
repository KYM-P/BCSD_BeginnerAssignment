package com.example.week13.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.week13.ui.item.WordItemData
import com.example.week13.ui.item.WordsRecyclerAdapter
import com.example.week13.databinding.ActivityMainBinding
import com.example.week13.ui.add.AddWordItemActivity
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    companion object {
        const val RESULT_ADD = 0
        const val RESULT_REVISE = 1
        const val RESULT_EMPTY = -1
    }

    private lateinit var binding : ActivityMainBinding // Activity 의 root 가 layout 일 때 작동
    private lateinit var viewModel : MainActivityModel

    val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val newWord = WordItemData(
            result.data?.getStringExtra("newWordName")?:"",
            result.data?.getStringExtra("newWordMean")?:""
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
        // viewModel
        binding.viewModel = MainActivityModel(MutableLiveData(""),
            MutableLiveData(""),
            -1,
            application)
        viewModel = binding.viewModel as MainActivityModel

        // activity
        binding.activity = this

        // liveData
        viewModel.selectedWordName.observe(this) {str ->
            binding.selectedWordNameTv.text = str
        }
        viewModel.selectedWordMean.observe(this) {str ->
            binding.selectedWordMeanTv.text = str
        }

        // set recyclerView
        val adapter = WordsRecyclerAdapter(viewModel.wordsList , this)
        binding.wordsRcv.adapter = adapter
        binding.wordsRcv.addItemDecoration(DividerItemDecoration(this, 1))

        // set recyclerView items from Database
        runBlocking {
            viewModel.setItemsFromDb()
            adapter.notifyDataSetChanged()
        }
    }

    // detectSelectedWord */ recyclerView Item Click
    fun detectSelectedWord(view: View) {
        var position = binding.wordsRcv.getChildAdapterPosition(view)
        viewModel.updateSelectedData(viewModel.getWord(position).wordName,
            viewModel.getWord(position).wordMean,
            position
        )
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