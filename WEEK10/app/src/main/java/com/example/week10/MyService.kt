package com.example.week10

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyService : Service() {

    companion object {
        const val CHANNEL_ID = "MY_CHANNEL_1"
        const val ACTION_PLAY = "com.music.PLAY"
        const val ACTION_REPLAY = "com.music.REPLAY"
        const val ACTION_PAUSE = "com.music.PAUSE"
        const val ACTION_STOP = "com.music.STOP"
        const val N_CHANNEL = "Notification_Ch1"
    }

    private var mediaPlayer : MediaPlayer? = null

    private lateinit var broadcastReceiver : BroadcastReceiver

    private var musicName = ""
    private var isPause = false
    private var battery = 0

    override fun onCreate() {
        super.onCreate()

        // Channel 생성
        createNotificationChannel()

        // broadcast filter 설정
        val filter = IntentFilter().apply {
            addAction(ACTION_PLAY)
            addAction(ACTION_REPLAY)
            addAction(ACTION_PAUSE)
            addAction(ACTION_STOP)
            //addAction(Intent.ACTION_BATTERY_LOW) // 미사용
            //addAction(Intent.ACTION_BATTERY_OKAY) // 미사용
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        // broadcast receiver 설정
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    ACTION_REPLAY -> replayMusic()
                    ACTION_PAUSE -> pauseMusic()
                    ACTION_STOP -> stopMusic()
                    Intent.ACTION_BATTERY_CHANGED -> { // 현재 배터리 용량 갱신
                        battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                        notificationUpdate()
                    }
                }
            }
        }
        // broadcast receiver 생성
        registerReceiver(broadcastReceiver, filter, Context.RECEIVER_EXPORTED)
    }

    // broadcast receiver 제거
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    // Service 시작 함수를 받았을 때
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if(intent.getParcelableExtra("MusicUri", Uri::class.java) != null &&
                intent.getStringExtra("MusicName") != null) {
                playMusic(intent.getParcelableExtra("MusicUri", Uri::class.java)!!, intent.getStringExtra("MusicName")!!)
            }
            else{
                Log.e("MY_TAG","수신 데이터 없음")
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    // 음악을 처음 시작할 때
    fun playMusic(uri : Uri, name : String) {
        musicName = name
        isPause = false

        // notification 실행
        notificationUpdate()

        val attributes = AudioAttributes.Builder().apply {
            setUsage(AudioAttributes.USAGE_MEDIA)
            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        }.build()

        // 이미 다른 노래가 실행중이면 중단
        mediaPlayer?.apply {
            stop()
            release()
        }

        // 새로운 노래 시작
        mediaPlayer = MediaPlayer().apply {
            setDataSource(applicationContext, uri)
            setAudioAttributes(attributes)
            prepare()
            seekTo(0)
            start()
        }
    }

    // 음악 재시작
    fun replayMusic() {
        if (isPause) {
            mediaPlayer?.apply {
                start()
            }
            isPause = false
        }
        // isPause 정보를 갱신하기 위한 notification 갱신
        notificationUpdate()
    }

    // 음악 일시 정지
    fun pauseMusic() {
        if (!isPause) {
            mediaPlayer?.apply {
                pause()
            }
            isPause = true
        }
        // isPause 정보를 갱신하기 위한 notification 갱신
        notificationUpdate()
    }

    // 음악 중단
    fun stopMusic() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        isPause = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun notificationUpdate() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("PlayMusicName", musicName)
            putExtra("PlayMusicPaused", isPause)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        // notification 생성
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle(musicName)
            setContentText("Battery : $battery%") // 현재 배터리 용량 표시
            setSmallIcon(R.drawable.icon_shape)
            setContentIntent(PendingIntent.getActivity(baseContext,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
            setAutoCancel(false)
        }.build()

        // notification 실행
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            startForeground(1, notification)
        } else {
            startForeground(1, notification,
                FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        }
    }

    // 채널 생성
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = N_CHANNEL
            val descriptionText = "Test Notification" // 알림 채널 설명
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}