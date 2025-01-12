package com.example.week11

class Time (
    var mm: Int,
    var ss: Int,
    var ms: Int
) {
    companion object {
        const val MAX_MS_VALUE = 99
        const val MAX_SS_VALUE = 59
        const val MAX_MM_VALUE = 59
    }
    private var isMax = false

    override fun toString(): String {
        // toString().padStart(2, '0') 총 2 자리수가 되도록 앞에서 부터 '0' 생성 / 반대로 padEnd 도 존재
        return "${mm.toString().padStart(2, '0')} : ${ss.toString().padStart(2, '0')} : ${ms.toString().padStart(2, '0')}"
    }
    fun reset() {
        mm = 0
        ss = 0
        ms = 0
        isMax = false
    }
    fun addMs(){
        if (!isMax){
            ms++
            updateSs()
        }
    }
    private fun updateSs(){
        if(ms > MAX_MS_VALUE) {
            ms -= MAX_MS_VALUE
            ss++
        }
        updateMm()
    }
    private fun updateMm() {
        if (ss > MAX_SS_VALUE) {
            ss -= MAX_SS_VALUE
            mm++
        }
        checkMax()
    }
    private fun checkMax() {
        if (mm == MAX_MM_VALUE &&
            ss == MAX_SS_VALUE &&
            ms == MAX_MS_VALUE) {
            isMax = true
        }
    }
}
