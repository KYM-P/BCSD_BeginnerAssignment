package com.example.data.word.local.room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.word.model.WordItem

@Entity
data class WordItemDataModel (
    var wordName : String,
    var wordMean : String,
    var wordImg : String
) {
    @PrimaryKey(autoGenerate = true) var id : Int = 0

    fun dataTooDomainModel() : WordItem {
        return WordItem(wordName, wordMean, Uri.parse(wordImg))
    }
    companion object {
        fun domainToDataModel(wordItem : WordItem) : WordItemDataModel {
            return WordItemDataModel(wordItem.wordName, wordItem.wordMean, wordItem.wordImg.toString())
        }
    }
}