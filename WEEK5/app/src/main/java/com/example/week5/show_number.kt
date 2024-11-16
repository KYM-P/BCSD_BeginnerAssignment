package com.example.week5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.awt.font.TextAttribute
import kotlin.math.log
import kotlin.random.Random

class show_number : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_number)

        // v 주석 / i Info(정보) / d Debug(디버그) / w Warning(경고) / e Error(오류)

        var randomNumber : Int
        try {
            randomNumber = Random.nextInt(0,intent.getIntExtra("maxNumber",1));
        }catch (e: Exception) {
            randomNumber = 0;
        }
        val show_tv_value : TextView = findViewById(R.id.tv_show_value)
        show_tv_value.text = randomNumber.toString()
        intent.putExtra("randomNumber", randomNumber)
        setResult(RESULT_OK, intent) // result setting
        //finish() // 모든 엑티비티 종료
    }
}