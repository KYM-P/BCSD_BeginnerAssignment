package com.example.week14.ui.main

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import com.example.domain.word.model.WordItem
import com.example.domain.word.usecase.GetWordItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainActivityModel @Inject constructor (
    private val getWordItemUseCase : GetWordItemUseCase,
) : ViewModel() {
    var selectedWordName: MutableLiveData<String> = MutableLiveData("")
    var selectedWordMean: MutableLiveData<String> = MutableLiveData("")
    var selectedWordImg: MutableLiveData<Uri?> = MutableLiveData(null)
    var selectedWordPosition : Int = -1
    var wordsList = ArrayList<WordItem>()
    var isDataLoaded = MutableLiveData<Boolean>(false)

    /* init > viewModel 생성 후 실행 / onCreate 에서 실행 하도록 변경
    init {
        setItemsFromDb()
    }
     */

    override fun onCleared() {
        super.onCleared()
        runBlocking { // onCleared 전 실행 완료(동기) 보장 필요 > runBlocking / 단 Ui 작업이 block 되므로 주의
            updateAllFromDb()
        }
    }

    fun wordsListSize() : Int {
        return wordsList.size
    }

    fun getSelectedWord() : WordItem? {
        if(selectedWordPosition == -1) return null
        return wordsList[selectedWordPosition]
    }

    fun addWord(word : WordItem) {
        wordsList.add(word)
    }

    fun removeSelectedWord() {
        if(selectedWordPosition == -1) { return }
        wordsList.removeAt(selectedWordPosition)
        updateSelectedData("",
            "",
            null,
            -1
        )
    }

    fun updateSelectedWord(word : WordItem) {
        if(selectedWordPosition == -1) { return }
        updateSelectedData(word.wordName, word.wordMean, word.wordImg, selectedWordPosition)
        wordsList[selectedWordPosition].wordName = word.wordName
        wordsList[selectedWordPosition].wordMean = word.wordMean
        wordsList[selectedWordPosition].wordImg = word.wordImg
    }

    fun updateSelectedData(name : String, mean : String, img : Uri? , position : Int){
        selectedWordName.value = name
        selectedWordMean.value = mean
        selectedWordImg.value = img
        selectedWordPosition = position
    }

    fun getWord(position : Int) : WordItem {
        return wordsList[position]
    }

    fun setItemsFromDb() {
        viewModelScope.launch { // 비동기 실행.
            getWordItemUseCase.getWordItems().forEach { wordItem ->
                wordsList.add(wordItem)
            }
            Log.e("MY_TAG","Load end")
            isDataLoaded.postValue(true)
        }
    }

    suspend fun updateAllFromDb() {
        getWordItemUseCase.updateAllWordItems(wordsList)
        Log.e("MY_TAG","isSaved / items : ${wordsList.size}")
    }
}