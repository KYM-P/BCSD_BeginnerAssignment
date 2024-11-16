package com.example.bcsd_week3_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

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
        val toast = Toast.makeText(this, "Hi", Toast.LENGTH_LONG) // this = MainActivity
        //btnToLoginContraint.setOnLongClickListener{ toast.show() }
        //btnToLoginContraint.text = "Hi" // 아래와 기능적 동일 다만 메소드 보다 직접적인 변수 변경이 선호
        //btnToLoginContraint.setText("Hi")
    }

    fun toLoginContraint(){
        //intent.putExtra(key, value) // intent 에 데이터 입력 int, string, char, bundle 등 다양한 정보 가능
        //intent.getIntExtra(key) // intent 에 데이터 받아옴 - 내부에서 해당 값으로 Bundle 생성
        //intent.getBundleExtra(key) // intent 에 Bundle 데이터 받아옴
        // intent 와의 데이터 전달은 Bundle 을 이용하여 전달
        // resultLauncher.launcher(intent) // intent 에 데이터 전송도 가능
        startActivity(Intent(this, LoginActivityContraint::class.java)) // intent 로 인한 값 전달 불가
        // startActivityForResult(intent, Code) ->
        // resultLauncher - 되돌아올 때 데이터 전송(창이 닫혔을 때)
        // setResult ? RESULT_OK / RESULT_CANCLED
    }
    fun toLoginLinear(){
        startActivity(Intent(this, LoginActivityLinear::class.java))
    }
    fun toLoginRelative() {
        startActivity(Intent(this, LoginActivityRelative::class.java))
    }
}