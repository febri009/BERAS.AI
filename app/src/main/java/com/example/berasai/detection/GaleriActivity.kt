package com.example.berasai.detection

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.berasai.databinding.ActivityGaleriBinding
import java.io.IOException

class GaleriActivity : AppCompatActivity() {
    private lateinit var mClassifier: ClassificationFromGallery
    private lateinit var mBitmap: Bitmap

    private val mGalleryRequestCode = 2

    private val mInputSize = 224
    private val mModelPath = "Model_Kualitas_Beras_MobileNet.tflite"
    private val mLabelPath = "Klasifikasi_Kualitas_Beras.txt"
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var binding: ActivityGaleriBinding

    @SuppressLint("SetTextI18n", "Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGaleriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        mClassifier = ClassificationFromGallery(assets, mModelPath, mLabelPath, mInputSize)

        binding.mGalleryButton.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.mDetectButton.setOnClickListener {
            val results = mClassifier.recognizeImage(mBitmap).firstOrNull()
            binding.mResultTextView.text = "${results?.title}, akurasi: ${results?.percent}%"
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                try {
                    val descriptor = contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
                    descriptor?.let {
                        mBitmap = BitmapFactory.decodeFileDescriptor(descriptor)
                        mBitmap = scaleImage(mBitmap)
                        binding.mPhotoImageView.setImageBitmap(mBitmap)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("Recycle")
    @Suppress("DEPRECATION")
    @Deprecated(
        message = "This method is deprecated. Use registerForActivityResult instead.",
        level = DeprecationLevel.ERROR
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == mGalleryRequestCode && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    val descriptor = contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
                    descriptor?.let {
                        mBitmap = BitmapFactory.decodeFileDescriptor(descriptor)
                        mBitmap = scaleImage(mBitmap)
                        binding.mPhotoImageView.setImageBitmap(mBitmap)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun scaleImage(bitmap: Bitmap?): Bitmap {
        val originalWidth = bitmap!!.width
        val originalHeight = bitmap.height
        val scaleWidth = mInputSize.toFloat() / originalWidth
        val scaleHeight = mInputSize.toFloat() / originalHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true)
    }
}
