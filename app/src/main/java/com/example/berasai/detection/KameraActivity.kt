package com.example.berasai.detection


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.berasai.databinding.ActivityKameraBinding
import kotlinx.coroutines.*

class KameraActivity : AppCompatActivity() {

    private lateinit var classifier: ClassificationFromCamera
    private lateinit var binding: ActivityKameraBinding

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA_CODE) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Untuk menggunakan fitur ini Anda harus mengizinkan kamera",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            REQUEST_CAMERA_CODE
        )
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classifier = ClassificationFromCamera(assets)

        if (allPermissionsGranted()) {
            setupCamera()
        } else {
            requestCameraPermission()
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun setupCamera() {
        binding.camera.addPictureTakenListener { imageData ->
            lifecycleScope.launch(Dispatchers.Main) {
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

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (allPermissionsGranted()) {
            binding.camera.start()
        }
    }

    override fun onPause() {
        if (allPermissionsGranted()) {
            binding.camera.stop()
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (allPermissionsGranted()) {
            binding.camera.destroy()
        }
        super.onDestroy()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CAMERA_CODE = 1
    }

}












