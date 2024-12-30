package com.example.week7

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment // DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.week7.databinding.FragmentDialogBinding

class MyDialogFragment () : DialogFragment() { // 생성자를 통한 매개변수 받아오기 가능
    lateinit var binding: FragmentDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_dialog, container, false)
        binding = FragmentDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialog()
    }

    fun setDialog() {
        //dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) 해당 구문을 사용해 배경을 투명화 하여도 상관없음
        setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 닫히지 않음
        binding.btnDialogTrue.setOnClickListener {
            setFragmentResult("customDlg",
                Bundle().apply{
                    putString("changedName", binding.etDialogTitle.text.toString())
                })
            dismiss()
        }
        binding.btnDialogFalse.setOnClickListener {
            dismiss()
        }
    }
}