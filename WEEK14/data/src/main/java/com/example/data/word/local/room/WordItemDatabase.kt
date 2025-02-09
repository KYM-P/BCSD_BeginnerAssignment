package com.example.data.word.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [WordItemDataModel::class],
    version = 2, // img 추가로 version up
    exportSchema = false // schema false 설정 > 버전 관리를 위해 schema 폴더를 생성하는 방법을 더 선호
)
abstract class WordItemDatabase : RoomDatabase() {
    abstract fun WordItemDao(): WordItemDao
    companion object {
        val DATABASE_NAME: String = "database-word"

        @Volatile
        private var instance: WordItemDatabase? = null

        private fun buildDatabase(context: Context): WordItemDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                WordItemDatabase::class.java,
                DATABASE_NAME
            ).build()
        fun getInstance(context: Context): WordItemDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it } // instance 가 null 이면 DataBase 생성 후 대입
            }

        val MIGRATION_1to2 = object : Migration(1, 2) { // 기존 데이터 마이그래이션 / 오류로 비사용
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("")
            }
        }
    }
}