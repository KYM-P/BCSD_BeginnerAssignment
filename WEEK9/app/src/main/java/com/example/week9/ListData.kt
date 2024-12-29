package com.example.week9

import android.net.Uri

data class ListData (
    val uri : Uri,
    val type : String,
    val duration : Long,
    val name : String,
    val artist : String,
    val album : String
)