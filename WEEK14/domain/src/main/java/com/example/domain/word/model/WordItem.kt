package com.example.domain.word.model

import android.net.Uri

data class WordItem (
    var wordName : String,
    var wordMean : String,
    var wordImg : Uri?
) {
}