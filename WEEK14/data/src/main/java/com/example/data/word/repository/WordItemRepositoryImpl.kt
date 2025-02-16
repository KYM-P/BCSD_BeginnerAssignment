package com.example.data.word.repository

import com.example.data.word.local.WordItemLoaclDataSource
import com.example.data.word.local.room.WordItemDataModel
import com.example.domain.word.model.WordItem
import com.example.domain.word.repository.WordItemRepository
import javax.inject.Inject

class WordItemRepositoryImpl @Inject constructor (
    private val wordItemLoaclDataSource: WordItemLoaclDataSource
) : WordItemRepository {
    override suspend fun getAllWordItems() : ArrayList<WordItem> {
        val wordItemDbList = wordItemLoaclDataSource.getAllWordItemsFromDb()
        val wordItemList = ArrayList<WordItem>()
        if(wordItemDbList != null) {
            wordItemDbList.forEach { wordItemList.add(it.dataTooDomainModel()) }
        }
        return wordItemList
    }
    override suspend fun deleteAllWordItems() {
        wordItemLoaclDataSource.deleteAllWordItemsInDb()
    }
    override suspend fun saveAllWordItems(wordItems : ArrayList<WordItem>) {
        wordItems.forEach{
            insertWordItem(it)
        }
    }
    suspend fun insertWordItem(wordItem: WordItem) {
        wordItemLoaclDataSource.insertWordItemToDb(WordItemDataModel.domainToDataModel(wordItem))
    }
}