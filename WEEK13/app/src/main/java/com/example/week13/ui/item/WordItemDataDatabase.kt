package com.example.week13.ui.item

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WordItemData::class],
    version = 1
)
abstract class WordItemDataDatabase : RoomDatabase() {
    abstract fun WordItemDataDao(): WordItemDataDao
    companion object {
        private var instance: WordItemDataDatabase? = null

        private fun buildDatabase(context: Context): WordItemDataDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                WordItemDataDatabase::class.java,
                "database-word"
            ).build()
        fun getInstance(context: Context): WordItemDataDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it } // instance 가 null 이면 DataBase 생성 후 대입
            }
    }
}