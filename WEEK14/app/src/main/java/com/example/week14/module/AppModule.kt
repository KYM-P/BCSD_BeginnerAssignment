package com.example.week14.module

import android.content.Context
import com.example.data.word.local.WordItemLoaclDataSource
import com.example.data.word.local.WordItemLocalDataSourceImpl
import com.example.data.word.local.room.WordItemDao
import com.example.data.word.local.room.WordItemDatabase
import com.example.data.word.repository.WordItemRepositoryImpl
import com.example.domain.word.repository.WordItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWordItemDatabase(@ApplicationContext context: Context) : WordItemDatabase {
        return WordItemDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideWordItemLoclDao(
        wordItemDatabase : WordItemDatabase
    ) : WordItemDao {
        return wordItemDatabase.WordItemDao()
    }

    @Provides
    @Singleton
    fun provideWordItemLoclDataSource(
        wordItemDao : WordItemDao
    ) : WordItemLoaclDataSource {
        return WordItemLocalDataSourceImpl(wordItemDao)
    }

    @Provides
    @Singleton
    fun provideWordItemRepository(
        wordItemLoaclDataSource: WordItemLoaclDataSource
    ) : WordItemRepository {
        return WordItemRepositoryImpl(wordItemLoaclDataSource)
    }
}