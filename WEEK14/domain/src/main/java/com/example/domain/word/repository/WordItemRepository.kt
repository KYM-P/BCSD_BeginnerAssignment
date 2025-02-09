package com.example.domain.word.repository

import com.example.domain.word.model.WordItem

interface WordItemRepository {
    suspend fun getAllWordItems() : ArrayList<WordItem>
    suspend fun deleteAllWordItems()
    suspend fun saveAllWordItems(wordItems : ArrayList<WordItem>)
}