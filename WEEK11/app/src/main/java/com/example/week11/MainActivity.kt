package com.example.week11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.week11.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var job : Job? = null
    //private var pauseResult = CompletableDeferred<Unit>() // CompletableDeferred 을 통한 결과값 반환 방식 > 한번 결과값이 반환되면 다시 재설정 해야함 > 비효율적 > channel 방식으로 변경
    private val pauseChannel = Channel<Unit>(1) // 용량 1인 channel 생성
    private var listData = arrayListOf<ListData>()
    private val time = Time(0,0,0)
    private var isPause = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TextView 로 보이는 초기 time 설정
        binding.tvTime.text = time.toString()

        // ListView 와 Adapter 연결
        val listAdapter = LAdapter(listData)
        binding.lvLabList.adapter = listAdapter
        listData.add(ListData("Lab_${listData.size}",time.toString()))

        // 일반 button 과 toggle 변수를 통해 생성 / 대체 : ToggleButton 로 제작 가능
        binding.btnStartAndPause.setOnClickListener {
            isPause = !isPause
            when {
                // 타이머 일시 정지
                isPause -> {
                    binding.btnStartAndPause.text = getString(R.string.st_start)
                }
                // 타이머 작동
                else -> {
                    if(job != null) {
                        resumeScope()
                    }else {
                        Toast.makeText(this,"start",Toast.LENGTH_SHORT).show()
                        startScope()
                    }
                    binding.btnStartAndPause.text = getString(R.string.st_pause)
                }
            }
        }
        // Stop 버튼 설정
        binding.btnStop.setOnClickListener {
            Toast.makeText(this,"stop",Toast.LENGTH_SHORT).show()
            runBlocking {
                endScope()
                job?.join() // job 이 종료될 때 까지 멈춤
                time.reset()
            }
            binding.tvTime.text = time.toString()
        }
        // Lab 버튼 설정
        binding.btnLab.setOnClickListener {
            listData.add(ListData("Lab_${listData.size}",time.toString()))
            listAdapter.notifyDataSetChanged()
        }
    }

    fun startScope() {
        val scope = lifecycleScope
        //scope = CoroutineScope(Dispatchers.Main) // 전체 scope / 대체: lifecycleScope 를 사용하면 Activity 와 동일한 cycle 사용
        job = scope?.launch {
            while (isActive) {
                //if(isPause) pauseResult.await() // CompletableDeferred 방식 > 비사용, 메모용 코드
                if(isPause) pauseChannel.receive() // pause 시 대기 상태 > channel 에 반환 값이 올 때 까지 대기
                delay(10)
                time.addMs()
                binding.tvTime.text = time.toString()
            }
        }
    }
    fun resumeScope() {
        //pauseResult.complete(Unit) // CompletableDeferred 방식 > 비사용, 메모용 코드
        pauseChannel.trySend(Unit) // channel 에 반환값 전달 > 용량이 넘치면 실패 후 false 반환 > 단 전달 값은 필요없으므로 그냥 사용 *받았다는 수신 여부만 필요
    }
    fun endScope() {
        isPause = true
        binding.btnStartAndPause.text = getString(R.string.st_start)
        job?.cancel() // cancel 시 다시 launch 할 수 없다
        job = null
    }
    /* CoroutineScope(Dispatchers.Main) 방식 > 메모용 코드
    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }
     */
}