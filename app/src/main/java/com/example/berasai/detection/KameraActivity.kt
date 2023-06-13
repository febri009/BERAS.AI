package com.example.berasai.detection


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.berasai.databinding.ActivityKameraBinding
import kotlinx.coroutines.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class KameraActivity : AppCompatActivity() {

    private lateinit var classifier: ClassificationFromCamera
    private lateinit var binding: ActivityKameraBinding

    private val executor = ThreadPoolExecutor(
        2, // Jumlah thread dalam pool
        2, // Jumlah maksimal thread dalam pool
        0L, // Waktu tunggu sebelum membuang thread yang tidak digunakan
        TimeUnit.MILLISECONDS, // Satuan waktu tunggu
        LinkedBlockingQueue() // Antrian tugas yang akan dijalankan
    )

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classifier = ClassificationFromCamera(assets)

        if (hasCameraPermission()) {
            setupCamera()
        } else {
            requestCameraPermissions()
        }
    }

    private fun requestCameraPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_CODE
        )
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun setupCamera() {
        binding.camera.addPictureTakenListener { imageData ->
            executor.execute {
                val recognitions = classifier.recognize(imageData.data)
                val txt = recognitions.joinToString(separator = "\n\n\n\n")

                runOnUiThread {
                    binding.tvHasil1.text = txt
                }
            }
        }
        binding.capturePhoto.setOnClickListener {
            binding.camera.capture()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCamera()
            } else {
                Toast.makeText(
                        this,
                        "Aplikasi perlu izin penggunaan kamera",
                        Toast.LENGTH_LONG
                ).show()
                requestCameraPermissions()
            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (hasCameraPermission()) {
            binding.camera.start()
        }
    }

    override fun onPause() {
        if (hasCameraPermission()) {
            binding.camera.stop()
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (hasCameraPermission()) {
            binding.camera.destroy()
        }
        super.onDestroy()
    }

    private fun hasCameraPermission() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val REQUEST_CAMERA_CODE = 1
    }
}






