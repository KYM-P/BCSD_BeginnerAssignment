package com.example.week6

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week6.databinding.ActivityMainBinding
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.internal.TextWatcherAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    val LogTag = "My_Tag"

    var maxNumber : Int = 0

    // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.v(LogTag,"insert onCreate")

        // 부분 문자열의 색상 변경 > SpannableString
        binding.tvMainValue.text = maxNumber.toString()

        var toast = Toast.makeText(this, "Dialog", Toast.LENGTH_SHORT)
        val builder = AlertDialog.Builder(this)
        //val items = arrayOf<String>("apple", "banana", "peach", "lemon", "orange")
        //val booleans = Array<Boolean>(items.size) { false }.toBooleanArray()
        binding.btnDialog.setOnClickListener {
            setDialog(builder, toast)
            //setListDialog(builder, items)
            //setCheckDialog(builder, items, booleans)
            //setRadioDialog(builder, items)
            //setCustomDialog()
        }

        binding.btnCount.setOnClickListener {
            ++maxNumber
            binding.tvMainValue.text = maxNumber.toString()
        }

        binding.etToast.addTextChangedListener(
            @SuppressLint("RestrictedApi") // 현재 프로젝트의 minSDKVersion 이후에 나온 API 사용시 Lint 로 해당 API 선언해 경고 문구 제거 (TextWatcherAdapter() 에 필요)
            object : TextWatcherAdapter() { // TextWatcherAdapter()를 통해 필요한 부분만 재 정의하여 사용 가능
                override fun afterTextChanged(s: Editable) {
                    toast.setText(binding.etToast.text) // p0.toString() 사용 가능
                }
        })
        binding.btnRandom.setOnClickListener {
            setFragment(BlankFragment(), Bundle().apply{putInt("RandNumber",maxNumber)})
        }
        // Fragment 의 Result 수집
        supportFragmentManager.setFragmentResultListener("RandBundle", this) {Key, bundle ->
            val result = bundle.getInt("ResultNumber")
            maxNumber = result
            binding.tvMainValue.text = maxNumber.toString()
        }
        // Fragment 가 있을 때 Main Activity 도 동작중이므로 여기서도 Fragment 종료 가능 단 본래의 Activity callBack 이 소멸 > Back 버튼으로 종료 불가
        //onBackPressedDispatcher.addCallback { supportFragmentManager.popBackStack() }
    }

    private fun setFragment(frag : Fragment, bundle: Bundle?) {  //2번
        supportFragmentManager.commit {
            frag.arguments = bundle // arguments 에 Bundle 을 담아 전송
            replace(R.id.fragment_show_random, frag) // add() 대신 replace()
            setReorderingAllowed(true)
            addToBackStack("replacement") // BackStack 에 쌓음
        }
    }

    // AlertDialog / 기본 다이얼로그 / 제목, 버튼, 최대 3개
    private fun setDialog(builder: AlertDialog.Builder, toast: Toast){
        builder.setTitle("Title")
            .setMessage("Message")
            .setIcon(R.drawable.vector_et)
            .setCancelable(false) // false : 뒤로가기, 바깥 터치에 닫히지 않음
            .setOnDismissListener {  } // 다이얼로그 dismiss 시
            .setPositiveButton("초기화",
                DialogInterface.OnClickListener { dialog, id ->
                    binding.tvMainValue.text = "0"
                    maxNumber = 0
                })
            .setNeutralButton("Toast",
                DialogInterface.OnClickListener { dialog, id ->
                    toast.show()
                })
            .setNegativeButton("종료",
                DialogInterface.OnClickListener { dialog, id ->
                })
        builder.show()
    }

    /* 과제 외 */
    // AlertDialog / 기본 다이얼로그 / 제목, 리스트
    private fun setListDialog(builder: AlertDialog.Builder, items: Array<String>){
        builder.setTitle("타이틀 입니다.")
            .setIcon(R.drawable.vector_et)
            .setItems(items, object: DialogInterface.OnClickListener { // setMessage 와 겹치면 안됨
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val snackbar = Snackbar.make(binding.root, "You choose ${items[p1]}, index $p1", Snackbar.LENGTH_SHORT)
                        .setTextColor(getResources().getColor(R.color.my_b_gray,theme))
                    snackbar.setAction("Yes", View.OnClickListener { snackbar.dismiss() })
                    snackbar.show()
                }
            })
            .setNegativeButton("Close", null)
        builder.show()
    }

    // AlertDialog / 기본 다이얼로그 / 제목, 리스트, BooleanArray / 중복 선택
    private fun setCheckDialog(builder: AlertDialog.Builder, items: Array<String> , booleans: BooleanArray){
        builder.setTitle("타이틀 입니다.")
            .setIcon(R.drawable.vector_et)
            .setMultiChoiceItems(items, booleans , object: DialogInterface.OnMultiChoiceClickListener { // booleans 가 필요 없다면 null 도 가능
                    override fun onClick(p0: DialogInterface?, p1: Int, p2: Boolean) {
                        booleans[p1] = p2
                    }
                })
            .setNegativeButton("Close", null)
        builder.show()
    }
    // AlertDialog / 기본 다이얼로그 / 제목, 리스트 / 단일 선택
    private fun setRadioDialog(builder: AlertDialog.Builder, items: Array<String>){
        builder.setTitle("타이틀 입니다.")
            .setIcon(R.drawable.vector_et)
            .setSingleChoiceItems(items, -1 , object: DialogInterface.OnClickListener { // -1 은 선행 checkedItem 이 없다는 뜻
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val snackbar = Snackbar.make(binding.root, "You choose ${items[p1]}, index $p1", Snackbar.LENGTH_SHORT)
                        .setTextColor(getResources().getColor(R.color.my_b_gray,theme))
                    snackbar.setAction("Yes", View.OnClickListener { snackbar.dismiss() })
                    snackbar.show()
                }
            })
            .setNegativeButton("Close", null)
        builder.show()
    }
    // DialogFragment / 커스텀 다이얼로그 /
    private fun setCustomDialog(){ // Activity 의 생명주기 내에 속하는 Fragment 이므로 해당 Activity 에서만 선언 가능
        val dialog = MyDialogFragment()
        dialog.show(supportFragmentManager,"")
    }
}