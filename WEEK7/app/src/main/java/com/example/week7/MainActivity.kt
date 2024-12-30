package com.example.week7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week7.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var lastLongClickPosition : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val array : ArrayList<ListData> = ArrayList() // 초기 리스트
        binding.rvList.layoutManager = LinearLayoutManager(this) // GridLayoutManager 등 존재
        binding.rvList.apply {
            adapter = RlistAdapter(array, deleteList(array), changeList())
            //addItemDecoration(DividerItemDecoration(context,1)) // 0 = horizontal / 0 = vertical / 기본 구분자
            addItemDecoration(CustomDivider(100,10, getColor(R.color.my_b_gray)))
        }

        //binding.rvList.addOnItemTouchListener() // touchListener 으로 onClick 대체 가능

        binding.btnAdd.setOnClickListener {
            if(binding.etListName.text.toString() != ""){
                array.add(ListData(binding.etListName.text.toString()))
                binding.rvList.adapter?.notifyItemInserted(array.size - 1) // 추가된 data 만 재구성
                binding.rvList.invalidateItemDecorations() // notifyItemInserted 로 생기는 ItemDecorations 의 미적용 해결
            }
            // binding.rvList.adapter?.notifyDataSetChanged() // 모든 Data 를 재구성 > 비효율적
        }
        supportFragmentManager.setFragmentResultListener("customDlg", this) {Key, bundle ->
            val result = bundle.getString("changedName")
            result?.run{
                array.get(lastLongClickPosition).tvName = result!!
                binding.rvList.adapter?.notifyItemChanged(lastLongClickPosition)
                binding.rvList.invalidateItemDecorations()
            }
        }
    }
    fun deleteList(array : ArrayList<ListData>) : (View) -> Unit {
        return { view : View ->
            val position = binding.rvList.getChildAdapterPosition(view)
            array.removeAt(position)
            binding.rvList.adapter?.notifyItemRemoved(position)
            binding.rvList.invalidateItemDecorations()
        }
    }
    inner class changeList {
        fun getLastLongClickViewPosition(view : View) {
            lastLongClickPosition = binding.rvList.getChildAdapterPosition(view)
        }
        fun setCustomDialog(){
            val dialog = MyDialogFragment()
            dialog.show(supportFragmentManager,"")
        }
    }
}