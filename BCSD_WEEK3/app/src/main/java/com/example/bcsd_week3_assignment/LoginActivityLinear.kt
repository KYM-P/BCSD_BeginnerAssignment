package com.example.bcsd_week3_assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivityLinear : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_linear)
        val btnToMain : Button = findViewById(R.id.btn_to_main)
        btnToMain.setOnClickListener{toMain()}
    }
    fun toMain(){
        finish()
        //finish() 현재 Activity 종료
        //finishAffinity() 스택의 모든 Activity 종료
    }
}