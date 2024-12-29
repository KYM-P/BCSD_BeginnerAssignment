package com.example.week8

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.text.Editable
import android.widget.Toast
// Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
// permission
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
// broadcast
import android.content.BroadcastReceiver
// drawable bitmap
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
// resultPendingIntent
import androidx.core.app.TaskStackBuilder
// binding 등
import com.example.week8.databinding.ActivityMainBinding
import com.google.android.material.internal.TextWatcherAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var builder : NotificationCompat.Builder

    companion object { // 상수
        const val PERMISSION_REQUEST_CODE = 5000 // permission Code
        const val CHANNEL_ID = "MY_CHANNEL_1"
    }

    private var maxNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionCheck() // 알람 권한 요구
        createNotificationChannel() // notification 채널 생성

        // count 갱신
        maxNumber = intent.getIntExtra("randomNumber", 0)
        binding.tvMainValue.text = maxNumber.toString()

        // toast 버튼
        var toast = Toast.makeText(this, "Hi", Toast.LENGTH_SHORT)
        binding.btnToast.setOnClickListener { toast.show() }
        binding.btnToast.setOnLongClickListener {
            Toast.makeText(this, "Hi-Long", Toast.LENGTH_LONG).show()
            return@setOnLongClickListener true
        }

        // count 버튼
        binding.btnCount.setOnClickListener {
            ++maxNumber
            binding.tvMainValue.text = maxNumber.toString()
        }

        // notification 버튼
        builder = NotificationCompat.Builder(this, CHANNEL_ID)
        binding.btnRandom.setOnClickListener {
            sendNotification()
        }

        // toast 메시지 변경
        binding.etToast.addTextChangedListener(@SuppressLint("RestrictedApi")
        object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable) {
                toast.setText(binding.etToast.text) // p0.toString() 사용 가능
            }
        })
    }

    // Android 8.0 이상에서 알림 제공 / notification 채널 생성
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification_Ch"
            val descriptionText = "Test Notification" // 알림 채널 설명
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission") // permission 요구 관련 문법 오류 무시
    private fun sendNotification() {
        // randomNumber를 보여주는 Activity 로의 intent
        val intent = Intent(this, show_number::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("maxNumber", maxNumber)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT) // PendingIntent.FLAG_UPDATE_CURRENT 미 사용시 intent 정보 갱신 불가
        /* TaskStackBuilder 으로 TaskStack 에 쌓은 형태로 제작 가능 / 단 이동할 activity 의 manifest 에 android:parentActivityName 선언 필요
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_MUTABLE)
        }
        다만 PendingIntent.getActivities 를 통해 intents 배열을 만들어 동일하게 구현 가능
         */

        // 닫기 버튼으로 알림 제거를 위한 intent
        val cancelIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = "CANCEL_NOTIFICATION" // tag 사용
        }
        val cancelpendingIntent = PendingIntent.getBroadcast(this, 0, cancelIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        //val bitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_shape); // png, jpg 등 이미지 파일이면 bitmap 으로 변환 가능
        val bitmap = drawableToBitmap(this, R.drawable.icon_shape) // 이미지 파일이 아닌 xml 이라면 canvas 에 그려서 직접 제작

        var builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.icon_shape_selected)
            setLargeIcon(bitmap)
            setContentTitle(getString(R.string.noti_title))
            setContentText(getString(R.string.noti_content)) // 확장시 세부 내용이 있다면 일반 내용은 안보임
            // setPriority(NotificationCompat.PRIORITY_LOW) channel 의 존재로 같을시 필요X
            setStyle(NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.noti_content_detail))) // 세부 내용, 확장시 보임
            setStyle(NotificationCompat.BigPictureStyle()
                .bigLargeIcon(null as? Bitmap) // 확장시 LargeIcon 이미지 변경 / bitmap 데이터인지 ic 데이터인지 구분 불가 > as? Bitmap 사용
                .bigPicture(bitmap))
            setContentIntent(pendingIntent)
            setShowWhen(true) // 타임 스탬프
            setAutoCancel(true)
            addAction(R.drawable.icon_shape, getString(R.string.noti_more), pendingIntent) // broadcast 와 연계 가능
            addAction(0, getString(R.string.noti_cancel), cancelpendingIntent) // 닫기 버튼
        }
        // notification 생성
        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build()) // permission 관련 문법 오류 발생 > if 문을 통한 검사 or 문법 오류 무시
        }
    }
    // xml drawable 을 canvas 로 그려 bitmap 데이터로 변환
    fun drawableToBitmap(context: Context, drawableId: Int): Bitmap {
        // Drawable 리소스
        val drawable: Drawable = context.getDrawable(drawableId) ?: throw IllegalArgumentException("Drawable not found")
        // 드로어블의 크기
        val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 1
        val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 1
        // 비트맵을 생성, 캔버스에 그림
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // 캔버스에 그리기 전 크기 조정
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    // broadcast 수신자 / mainActivity 에서만 사용하므로 우선 내부에 class 선언
    class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) { // 받았을 때
            if (intent.action == "CANCEL_NOTIFICATION") { // 알림 닫기 action 이면
                val notificationManager = context   .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(1) // 해당 id의 notification 제거
            }
        }
    }

    // permission (알람을 위한 알람 권한 요구)
    private fun permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 3.3 이상
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        }// 이외의 버전은 필요X
    }
    // 권한 요청 result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "Permission is denied", Toast.LENGTH_SHORT)
                        .show() // 권한 비 허용시 toast
                } else {
                    Toast.makeText(applicationContext, "Permission is granted", Toast.LENGTH_SHORT)
                        .show() // 권한 허용시 toast
                }
            }
        }
    }
}