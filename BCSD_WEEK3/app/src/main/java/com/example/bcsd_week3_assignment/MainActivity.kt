package com.example.bcsd_week3_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { // 앱이 최초 실행시
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // xml 화면 뷰를 연결

        val btnToLoginContraint : Button = findViewById(R.id.btn_contraint)
        btnToLoginContraint.setOnClickListener{toLoginContraint()}
        val btnToLoginLinear : Button = findViewById(R.id.btn_linear)
        btnToLoginLinear.setOnClickListener{toLoginLinear()}
        val btnToLoginRelative : Button = findViewById(R.id.btn_relative)
        btnToLoginRelative.setOnClickListener{toLoginRelative()}
    }

    fun toLoginContraint(){
        startActivity(Intent(this, LoginActivityContraint::class.java))
    }
    fun toLoginLinear(){
        startActivity(Intent(this, LoginActivityLinear::class.java))
    }
    fun toLoginRelative(){
        startActivity(Intent(this, LoginActivityRelative::class.java))
    }
}