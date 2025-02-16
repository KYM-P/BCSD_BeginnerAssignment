package com.example.data.word.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordItemDao {
    @Query("SELECT * FROM WordItemDataModel") // 테이블의 모든 값 가져오기
    suspend fun getAll(): List<WordItemDataModel>

    @Query("DELETE FROM WordItemDataModel WHERE wordName = :name & wordMean = :mean") // name, mean 과 같은 word 삭제
    suspend fun deleteWordBy(name : String, mean : String)

    @Query("DELETE FROM WordItemDataModel")
    suspend fun deleteALL()

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 동일 개체 존재시 덮어쓰기
    suspend fun insert(wordItemData: WordItemDataModel)

    @Update
    suspend fun update(wordItemData: WordItemDataModel)

    @Delete
    suspend fun delete(wordItemData: WordItemDataModel)
}