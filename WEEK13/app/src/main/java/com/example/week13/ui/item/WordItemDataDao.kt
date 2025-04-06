package com.example.week13.ui.item

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordItemDataDao {
    @Query("SELECT * FROM WordItemData") // 테이블의 모든 값 가져오기
    fun getAll(): List<WordItemData>

    @Query("DELETE FROM WordItemData WHERE wordName = :name & wordMean = :mean") // name, mean 과 같은 word 삭제
    fun deleteWordBy(name : String, mean : String)

    @Query("DELETE FROM WordItemData")
    fun deleteALL()

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 동일 개체 존재시 덮어쓰기
    fun insert(wordItemData: WordItemData)

    @Update
    fun update(wordItemData: WordItemData)

    @Delete
    fun delete(wordItemData: WordItemData)
}