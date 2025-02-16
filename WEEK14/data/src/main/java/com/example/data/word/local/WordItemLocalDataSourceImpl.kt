package com.example.data.word.local

import com.example.data.word.local.room.WordItemDao
import com.example.data.word.local.room.WordItemDataModel
import javax.inject.Inject

class WordItemLocalDataSourceImpl @Inject constructor(
    private val wordItemDao : WordItemDao
) : WordItemLoaclDataSource{
    override suspend fun getAllWordItemsFromDb() : List<WordItemDataModel> {
        return wordItemDao.getAll()
    }
    override suspend fun insertWordItemToDb(wordItemDataModel: WordItemDataModel) {
        wordItemDao.insert(wordItemDataModel)
    }
    override suspend fun deleteWordItemInDb(wordItemDataModel: WordItemDataModel) {
        wordItemDao.delete(wordItemDataModel)
    }
    override suspend fun deleteAllWordItemsInDb() {
        wordItemDao.deleteALL()
    }
    override suspend fun updateWordItemInDb(wordItemDataModel: WordItemDataModel) {
        wordItemDao.update(wordItemDataModel)
    }
}