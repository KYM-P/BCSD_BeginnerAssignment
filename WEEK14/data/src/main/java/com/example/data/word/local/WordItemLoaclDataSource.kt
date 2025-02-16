package com.example.data.word.local

import com.example.data.word.local.room.WordItemDataModel

interface WordItemLoaclDataSource{
    suspend fun getAllWordItemsFromDb() : List<WordItemDataModel>
    suspend fun insertWordItemToDb(wordItemDataModel: WordItemDataModel) : Unit
    suspend fun deleteWordItemInDb(wordItemDataModel: WordItemDataModel) : Unit
    suspend fun deleteAllWordItemsInDb() : Unit
    suspend fun updateWordItemInDb(wordItemDataModel: WordItemDataModel) : Unit
}