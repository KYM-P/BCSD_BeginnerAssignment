package com.example.week13.ui.item

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WordItemData (
    var wordName : String,
    var wordMean : String
) {
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}