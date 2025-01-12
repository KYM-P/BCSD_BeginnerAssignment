package com.example.week10

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week10.databinding.ActivityMainBinding
// permission api 31 이후
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Build

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val MusicList : ArrayList<ListData> = ArrayList() // 음악 리스트

    // Permission 요구 결과
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "this permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "this permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 0
        const val PERMISSION_CHECK_MODE_1 = 0
        const val PERMISSION_CHECK_MODE_2 = 1
        const val ACTION_PLAY = "com.music.PLAY"
        const val ACTION_REPLAY = "com.music.REPLAY"
        const val ACTION_PAUSE = "com.music.PAUSE"
        const val ACTION_STOP = "com.music.STOP"
    }

    // projection
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.MIME_TYPE,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ALBUM_ARTIST,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 재생 하단 바 안보임
        binding.layoutPlayer.isVisible = false

        // MainActivity 제거 후 Notification 으로 들어올 때
        if(intent.getStringExtra("PlayMusicName") != null) {
            //Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show()
            binding.layoutPlayer.isVisible = true
            binding.tvMusicName.text = intent.getStringExtra("PlayMusicName")
            binding.tvMusicName.isSelected = true
            binding.tgbtnPlayAndPause.isChecked = intent.getBooleanExtra("PlayMusicPaused", false)
        }

        // 실행 시 권한 허용 요청
        permissionCheck(PERMISSION_CHECK_MODE_1)

        // 권한이 없을 시 클릭할 tv
        binding.tvPermissionUncheckedClickable.setOnClickListener{
            permissionCheck(PERMISSION_CHECK_MODE_2)
        }
        // RecyclerView 설정
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.apply {
            adapter = RAdapter(MusicList, {listData : ListData -> playMusicService(listData)})
        }
        // toggle 버튼 설정
        binding.tgbtnPlayAndPause.setOnCheckedChangeListener { buttonView, isChecked ->
            when{
                isChecked -> {
                    val intent = Intent().apply { // Intent(this, MyService::class.java) 로 수신하면 MyService 에서 받지 못함
                        action = ACTION_PAUSE
                        addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
                    }
                    sendBroadcast(intent)
                }
                else -> {
                    val intent = Intent().apply { // Intent(this, MyService::class.java) 로 수신하면 MyService 에서 받지 못함
                        action = ACTION_REPLAY
                        addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
                    }
                    sendBroadcast(intent)
                }
            }
        }
        // stop 버튼 설정
        binding.btnStop.setOnClickListener {
            val intent = Intent().apply { // Intent(this, MyService::class.java) 로 수신하면 MyService 에서 받지 못함
                action = ACTION_STOP
                addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            }
            sendBroadcast(intent)
            binding.layoutPlayer.isVisible = false
            binding.tgbtnPlayAndPause.isChecked = false
        }
    }

    // Service 를 시작
    fun playMusicService(listData : ListData) {
        // Player 설정
        binding.layoutPlayer.isVisible = true
        binding.tvMusicName.text = listData.name
        binding.tvMusicName.isSelected = true
        // Service 를 시작할 intent
        val intent = Intent(this, MyService::class.java).apply { // Intent(this, MyService::class.java) 로 수신하면 MyService 에서 받지 못함
            action = ACTION_PLAY
            putExtra("MusicUri", listData.uri)
            putExtra("MusicName", listData.name)
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        }
        // Service 실행 (이미 실행중이면 새로운 Service 가 아닌 기존 Service 에서 onCommand 만 실행)
        startForegroundService(intent)
    }

    // 권한 요청 api 31 이후
    private fun permissionCheck(mode : Int) {
        val requiredPermissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 검사가 필요한 권한 추가
            requiredPermissions.add(android.Manifest.permission.READ_MEDIA_AUDIO)
            requiredPermissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
            // 현재 권한 검사, 거부시 허용 요청
            requiredPermissions.filter { permission ->
                ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
            }.forEach {permission ->
                // 2회 이상 dialog 허용 거부시 앱 권한 직접 부여만 가능 / shouldShowRequestPermissionRationale = dialog 로 요청 가능 시
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permission) && mode == PERMISSION_CHECK_MODE_2){
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${packageName}"))
                    startActivity(intent)
                }
                else{
                    requestPermissionLauncher.launch(permission) // 권한 요청
                }
            }
            // 허용 요청 이후 모든 권한이 있을 시
            if (requiredPermissions.filter { permission ->
                ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
            }.isEmpty()){
                permissionGrantedAction()
            }
        }
    }
    // 권한이 모두 허용되었을 때
    private fun permissionGrantedAction() {
        binding.tvPermissionUnchecked.isVisible = false
        binding.tvPermissionUncheckedClickable.isVisible = false
        setArray()
    }
    // 외부 데이터를 array 요소로 읽어와 변환
    private fun setArray() {
        // query
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection, // projection 에 선언된 column 만 받아옴
            null, // selection
            null, // selectionArgs
            null // sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE) // 확장자
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumArtistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
                val mineType = cursor.getString(mimeTypeColumn)?:"null"
                val duration = cursor.getLong(durationColumn)
                val name = cursor.getString(displayNameColumn)?:"null"
                val album = cursor.getString(albumColumn)?:"null"
                val artist = cursor.getString(albumArtistColumn)?:"null"
                // 얻은 데이터를 통해 리스트에 추가, 갱신
                MusicList.add(ListData(uri, mineType, duration, name, artist, album))
                binding.rvList.adapter?.notifyItemInserted(MusicList.size - 1)
            }
            cursor.close()
        }
    }
}