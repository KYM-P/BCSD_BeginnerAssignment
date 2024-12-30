package com.example.week8

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class show_number : AppCompatActivity() {

    private var randomNumber : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_number)

        randomNumber = Random.nextInt(0,intent.getIntExtra("maxNumber", 0) + 1)
        val show_tv_value : TextView = findViewById(R.id.tv_show_value)
        show_tv_value.text = randomNumber.toString()


        val resultIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("randomNumber", randomNumber)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        onBackPressedDispatcher.addCallback(this) {
            startActivity(resultIntent)
            finish() // 현재 Activity 종료
        }
    }
}