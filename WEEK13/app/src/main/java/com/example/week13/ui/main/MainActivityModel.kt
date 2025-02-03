package com.example.week13.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.week13.ui.item.WordItemData
import com.example.week13.ui.item.WordItemDataDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class MainActivityModel (
    var selectedWordName: MutableLiveData<String>,
    var selectedWordMean: MutableLiveData<String>,
    var selectedWordPosition : Int = -1,
    var application : Application
) : ViewModel() {
    private lateinit var db : WordItemDataDatabase// WordItemData 저장용 db
    var wordsList = ArrayList<WordItemData>()


    fun wordsListSize() : Int {
        return wordsList.size
    }

    fun getSelectedWord() : WordItemData? {
        if(selectedWordPosition == -1) return null
        return wordsList[selectedWordPosition]
    }

    fun addWord(word : WordItemData) {
        wordsList.add(word)
        addFromDb(word)
    }

    fun removeSelectedWord() {
        if(selectedWordPosition == -1) { return }
        deleteFromDb(wordsList[selectedWordPosition])
        wordsList.removeAt(selectedWordPosition)
        updateSelectedData("",
            "",
            -1
        )
    }

    fun updateSelectedWord(word : WordItemData) {
        if(selectedWordPosition == -1) { return }
        updateSelectedData(word.wordName, word.wordMean, selectedWordPosition)
        wordsList[selectedWordPosition].wordName = word.wordName
        wordsList[selectedWordPosition].wordMean = word.wordMean
        updateFromDb(wordsList[selectedWordPosition])
    }

    fun updateSelectedData(name : String, mean : String, position : Int){
        selectedWordName.value = name
        selectedWordMean.value = mean
        selectedWordPosition = position
    }

    fun getWord(position : Int) : WordItemData {
        return wordsList[position]
    }

    fun setItemsFromDb() : ArrayList<WordItemData> {
        CoroutineScope(Dispatchers.IO).launch { // 비동기 실행.
            db = WordItemDataDatabase.getInstance(application.applicationContext)
            ArrayList(db.WordItemDataDao().getAll()).forEach{
                addWord(it)
            }
        }
        return wordsList
    }

    private fun deleteFromDb(word : WordItemData) {
        CoroutineScope(Dispatchers.IO).launch {
            db.WordItemDataDao().delete(word)
        }
    }

    private fun addFromDb(word : WordItemData) {
        CoroutineScope(Dispatchers.IO).launch {
            db.WordItemDataDao().insert(word)
        }
    }

    private fun updateFromDb(word : WordItemData) {
        CoroutineScope(Dispatchers.IO).launch {
            db.WordItemDataDao().update(word)
        }
    }
    fun deleteAllFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            db.WordItemDataDao().deleteALL()
        }
    }
}