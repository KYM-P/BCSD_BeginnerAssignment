package com.example.week9

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
import com.example.week9.databinding.ActivityMainBinding
// permission api 31 이후
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val MusicList : ArrayList<ListData> = ArrayList() // 음악 리스트

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
            permissionGrantedAction()
        } else {
            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 0
        const val PERMISSION_CHECK_MODE_1 = 0
        const val PERMISSION_CHECK_MODE_2 = 1
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

        permissionCheck(PERMISSION_CHECK_MODE_1) // 실행 시 권한 허용 요청

        // 권한이 없을 시 클릭할 tv
        binding.tvPermissionUncheckedClickable.setOnClickListener{
            permissionCheck(PERMISSION_CHECK_MODE_2)
        }

        // RecyclerView 설정
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.apply {
            adapter = RAdapter(MusicList)
        }

    }

    // 권한 요청 api 31 이후
    private fun permissionCheck(mode : Int) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                permissionGrantedAction()
            }
            // 2회 이상 dialog 허용 거부시 앱 권한 직접 부여만 가능 / shouldShowRequestPermissionRationale = dialog 로 요청 가능 시
            !ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.READ_MEDIA_AUDIO) && mode == PERMISSION_CHECK_MODE_2 -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                    Uri.parse("package:${packageName}")
                )
                startActivity(intent)
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_AUDIO)
            }
        }
    }

    private fun permissionGrantedAction() {
        binding.tvPermissionUnchecked.isVisible = false
        binding.tvPermissionUncheckedClickable.isVisible = false
        setArray()
    }

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
                val mineType = cursor.getString(mimeTypeColumn)
                val duration = cursor.getLong(durationColumn)
                val name = cursor.getString(displayNameColumn)
                val album = cursor.getString(albumColumn)
                val artist = cursor.getString(albumArtistColumn)
                // 얻은 데이터를 통해 리스트에 추가, 갱신
                MusicList.add(ListData(uri, mineType, duration, name, artist, album))
                binding.rvList.adapter?.notifyItemInserted(MusicList.size - 1)
            }
            cursor.close()
        }
    }
}