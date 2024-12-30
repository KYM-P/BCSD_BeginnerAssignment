package com.example.week7

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.week7.databinding.ActivityItemBinding


class RlistAdapter(
    private val items: ArrayList<ListData>,
    val onClickDelete : (View) -> Unit,
    val onClickChange : MainActivity.changeList
) : RecyclerView.Adapter<RlistAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ActivityItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemName = binding.tvItemName
        var item = binding.llItem

        fun setItemClick(view : View) {
            view.setOnClickListener {
                val builder = AlertDialog.Builder(item.context) // 생명 주기
                setDialog(builder, itemView)
            }
        }
        // 구현 완료 > 단 비효율적인 방식같음
        fun setItemLongClick(view : View) {
            view.setOnLongClickListener {
                onClickChange.getLastLongClickViewPosition(itemView) // 현재 LongClick 한 view 를 MainActivity로 전송
                onClickChange.setCustomDialog()
                return@setOnLongClickListener true
            }
        }
    }
    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // viewHolder 생성마다 호출 / 데이터 바인딩 전 상태
        // inflate : xml 의 레이아웃을 메모리에 로딩 > 이후 객체화 작업 > 객체화 이후 사용 가능
        // inflate(View 로 생성할 xml, 생성할 View 의 부모, true 시 root 의 자식으로 할당)
        var binding = ActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding) // viewHolder 객체 생성
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // 데이터 연결시 호출 / 데이터 바인딩 이후 상태
        holder.apply {
            itemName.text = items[position].tvName
            setItemClick(item) // item 별 onClickListener 설정
            setItemLongClick(item)
        }
    }

    private fun setDialog(builder: AlertDialog.Builder, view : View){
        builder.setTitle("이름 목록 삭제하기")
            .setMessage("이름 목록을 삭제해보자")
            .setIcon(R.drawable.icon_shape)
            .setCancelable(false) // false : 뒤로가기, 바깥 터치에 닫히지 않음
            .setPositiveButton("삭제",
                DialogInterface.OnClickListener { dialog, id ->
                    onClickDelete.invoke(view)
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                })
        builder.show()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}