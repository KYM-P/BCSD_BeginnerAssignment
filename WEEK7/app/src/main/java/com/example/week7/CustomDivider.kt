package com.example.week7

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomDivider (
    private val VerticalPaddong : Int,
    private val HorizonPadding : Int,
    private val DividerColor : Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets( // 각 item 마다 적용
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //val totalItemCount = state.itemCount                //총 아이템 수
        //val scrollPosition = state.targetScrollPosition     //스크롤 됬을때 아이템 position
        val position : Int = parent.getChildAdapterPosition(view) //각 아이템뷰의 순서 (index)
        // set(left, top, right, bottom) / outRect 로 간격 설정
        outRect.set(HorizonPadding, 0 , if(position == state.itemCount) HorizonPadding else 0, VerticalPaddong) // 마지막 요소면 밑방향 padding X
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val paint = Paint().apply { color = DividerColor }

        val left : Float = parent.paddingStart + HorizonPadding.toFloat()
        val right : Float = parent.width - parent.paddingEnd - HorizonPadding.toFloat()
        val heigh = 5.0f

        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = (child.bottom + params.bottomMargin + (VerticalPaddong - heigh) / 2)
            val bottom = top + heigh

            c.drawRect(left, top, right, bottom, paint)
        }
    }
}