package com.example.domain.word.usecase

import com.example.domain.word.model.WordItem
import com.example.domain.word.repository.WordItemRepository
import javax.inject.Inject

class GetWordItemUseCase @Inject constructor (
    private val wordItemRepository: WordItemRepository
) {
    // suspend operator fun invoke() : ArrayList<WordItem> { // invoke 를 통해 class 이름으로 호출 가능 }
    suspend fun getWordItems() : ArrayList<WordItem> {
        return wordItemRepository.getAllWordItems()
    }
    suspend fun updateAllWordItems(wordItems : ArrayList<WordItem>) {
        wordItemRepository.deleteAllWordItems()
        wordItemRepository.saveAllWordItems(wordItems)
    }
}