package com.example.week5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
// resultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
// TextWatcher
import android.text.Editable
import android.text.TextWatcher

class MainActivity : AppCompatActivity() {
    val LogTag = "My_Tag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.v(LogTag,"insert onCreate")

        var maxNumber: Int = 0;
        // 부분 문자열의 색상 변경 > SpannableString
        val main_tv_max_num: TextView = findViewById(R.id.tv_main_value)
        main_tv_max_num.text = maxNumber.toString()

        val main_btn_toast: Button = findViewById(R.id.btn_toast)
        var toast = Toast.makeText(this, "Hi", Toast.LENGTH_SHORT) // 기본 toast 의 아이콘은 roundIcon 으로 설정되어 있음
        main_btn_toast.setOnClickListener { toast.show() }
        main_btn_toast.setOnLongClickListener {
            Toast.makeText(this, "Hi-Long", Toast.LENGTH_LONG).show()
            return@setOnLongClickListener true // setOnLongClickListener 은 boolean 값을 반환 해줘야 한다.
        }

        val main_btn_count: Button = findViewById(R.id.btn_count)
        main_btn_count.setOnClickListener {
            maxNumber++
            main_tv_max_num.text = maxNumber.toString()
        }

        val main_btn_random : Button = findViewById(R.id.btn_random)
        val resultLauncher = registerForActivityResult( // registerForActivityResult / ActivityResult, ActivityResultCallback 을 이용 한 ActivityResultLauncher 반환
            ActivityResultContracts.StartActivityForResult() // StartActivityForResult() 사용
        ) { result: ActivityResult -> // Callback 함수 / onActivityResult()
            if (result.resultCode == RESULT_OK) { // RESULT_OK 를 받았을 때 / RESULT_CANCELED 등 존재
                maxNumber = result.data?.getIntExtra("randomNumber", 0) ?: 0 // getIntExtra 는 default 값 추가 필요
                main_tv_max_num.text = maxNumber.toString()
            }
        }
        main_btn_random.setOnClickListener {
            val intent = Intent(this, show_number::class.java)
            intent.apply {
                this.putExtra("maxNumber", maxNumber)
                /*
                해당 메소드 등 사용 가능
                this.getBundleExtra("0")
                this.getIntExtra("0",0)
                this.getStringExtra("0")
                 */
            } // 해당 intent 에 적용한 메소드를 쉽게 확인할 수 있음
            resultLauncher.launch(intent)
        }
        val main_et_toast : EditText = findViewById(R.id.et_toast)
        // main_et_toast.setCompoundDrawables((Drawable), null , null, null) drawable 객체로 사용 가능
        main_et_toast.setCompoundDrawablesWithIntrinsicBounds( R.drawable.vector_et, 0, 0, 0); // 이미 같은 drawable 을 사용중이므로 코드상 의미 X
        main_et_toast.addTextChangedListener(object: TextWatcher  {
            /* 텍스트 입력 마다 before > on > after 순차 동작 */
            /* 텍스트 변경 전 호출 s:변경전 문자열, start:커서 시작 위치, before:변경 전 문자 수, after:변경 후 문자 수 */
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, after: Int) {
                // 해당 부분에서의 s 는 "Hi" 의 입력시 "H" 의 정보 존재
                Log.d(LogTag,"beforeT")
            }

            /* 텍스트 변경 시 호출 s:변경된 문자열, start:커서 시작 위치, before:변경 전 문자 수, after:변경 후 문자 수 */
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, after: Int) {
                // 해당 부분에서의 s 는 "Hi" 의 입력시 "Hi" 의 정보 존재
                Log.d(LogTag,"onT")
            }

            /* 텍스트 변경 이후 호출 s:변경된 문자열 */
            override fun afterTextChanged(s: Editable?) {
                // 해당 부분에서의 s 는 "Hi" 의 입력시 "Hi" 의 정보 존재
                Log.d(LogTag,"afterT")
                toast.setText(s.toString()) // 단 s.toString() 보다 main_et_toast.text 가 더 안정적
            }
        })
    }
    override fun onStart() {
        super.onStart()
        Log.v(LogTag,"insert onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.v(LogTag,"insert onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.v(LogTag,"insert onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.v(LogTag,"insert onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(LogTag,"insert onRestart")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.v(LogTag,"insert onDestroy")
    }
}