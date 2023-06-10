package com.example.berasai

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.io.IOException
import java.util.*

class GaleriActivity : AppCompatActivity() {
    private lateinit var mClassifier: KlasifikasiDariGaleri
    private lateinit var mBitmap: Bitmap

    private val mGalleryRequestCode = 2

    private val mInputSize = 224
    private val mModelPath = "Model_Kualitas_Beras_MobileNet.tflite"
    private val mLabelPath = "Klasifikasi_Kualitas_Beras.txt"
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    private var lastProcessingTimeMs: Long = 0

    @SuppressLint("SetTextI18n", "Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galeri)

        val mPhotoImageView = findViewById<ImageView>(R.id.mPhotoImageView)
        val mGalleryButton = findViewById<Button>(R.id.mGalleryButton)
        val mDetectButton = findViewById<CardView>(R.id.mDetectButton)
        val mResultTextView = findViewById<TextView>(R.id.mResultTextView)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        mClassifier = KlasifikasiDariGaleri(assets, mModelPath, mLabelPath, mInputSize)

        mGalleryButton.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        mDetectButton.setOnClickListener {
            val startTime = SystemClock.uptimeMillis()
            val results = mClassifier.recognizeImage(mBitmap).firstOrNull()
            mResultTextView.text = "${results?.title}\nProbabilitas: ${results?.percent}%"
            lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                try {
                    val descriptor = contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
                    descriptor?.let {
                        mBitmap = BitmapFactory.decodeFileDescriptor(descriptor)
                        mBitmap = scaleImage(mBitmap)
                        mPhotoImageView.setImageBitmap(mBitmap)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    }

    @SuppressLint("Recycle")
    @Suppress("DEPRECATION")
    @Deprecated(message = "This method is deprecated. Use registerForActivityResult instead.", level = DeprecationLevel.ERROR)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val mPhotoImageView = findViewById<ImageView>(R.id.mPhotoImageView)

        if (requestCode == mGalleryRequestCode && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    val descriptor = contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
                    descriptor?.let {
                        mBitmap = BitmapFactory.decodeFileDescriptor(descriptor)
                        mBitmap = scaleImage(mBitmap)
                        mPhotoImageView.setImageBitmap(mBitmap)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show()
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