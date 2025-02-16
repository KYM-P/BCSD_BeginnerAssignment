package com.example.week14.ui.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.week14.databinding.ActivityAddWordItemBinding
import com.example.week14.ui.main.MainActivity.Companion.RESULT_EMPTY
import com.example.week14.ui.main.MainActivity.Companion.RESULT_REVISE

class AddWordItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddWordItemBinding // Activity 의 root 가 layout 일 때 작동
    private val addModel = AddWordItemActivityModel("","",null)
    private var resultCode : Int = RESULT_EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWordItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // viewModel
        binding.viewModel = addModel

        // set view
        resultCode = intent.getIntExtra("resultCode", RESULT_EMPTY)
        when (resultCode) {
            RESULT_REVISE -> {
                updateAddModel(intent.getStringExtra("oldWordName")?:"",
                    intent.getStringExtra("oldWordMean")?:"",
                    Uri.parse(intent.getStringExtra("oldWordImg")?:"")
                )
            }
        }

        // word add btn
        binding.wordAddBtn.setOnClickListener {
            val intent = Intent().apply {
                putExtra("newWordName", binding.wordNameEt.text.toString())
                putExtra("newWordMean", binding.wordMeanEt.text.toString())
                putExtra("newWordImg", addModel.newWordImg.toString())
            }
            setResult(resultCode, intent)
            finish()
        }

        // gallery 용 Launcher
        val galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let { uri ->
                    // 영구적 uri 권한 획득
                    contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    // Glide (uri 를 불러와 적용)
                    updateWordImgIv(uri)
                    addModel.newWordImg = uri
                }
            }
        }
        // word img add btn
        binding.wordImgAddBtn.setOnClickListener {
            //val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) // googlePhoto 방식
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT) // SAF 방식
            intent.type = "image/*"  // 이미지 파일만 선택
            galleryLauncher.launch(intent) // mediaStore 으로 permission 미필요
        }
    }
    private fun updateAddModel(name : String, mean : String, img : Uri?){
        addModel.newWordName = name
        addModel.newWordMean = mean
        addModel.newWordImg = img
        updateWordImgIv(img)
        binding.invalidateAll()
    }
    private fun updateWordImgIv(img : Uri?) {
        Glide.with(this)
            .load(img)
            .into(binding.wordImgIv) // 적용할 ImageView
    }
}