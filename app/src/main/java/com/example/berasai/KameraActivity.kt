package com.example.berasai

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import com.example.berasai.databinding.ActivityKameraBinding
import com.priyankvasa.android.cameraviewex.CameraView
import kotlinx.coroutines.*
import java.util.*



class KameraActivity : AppCompatActivity() {

    private lateinit var classifier: Klasifikasi
    private lateinit var resultbar: TextView
    private lateinit var camera: CameraView
    private lateinit var binding: ActivityKameraBinding

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultbar = binding.tvHasil1
        classifier = Klasifikasi(assets)
        camera = binding.camera

        if (!canUseCamera()) {
            requestCameraPermissions()
        } else {
            setupCamera()
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
        binding.camera.addPictureTakenListener { image ->
            try {
                GlobalScope.launch(Dispatchers.IO) {
                    val recognitions = classifier.recognize(image.data)
                    val txt = recognitions.joinToString(separator = "\n")
                    withContext(Dispatchers.Main) {
                        resultbar.text = txt
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "ERROR: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("CameraError", "Error capturing image", e)
            }
        }

        binding.capturePhoto.setOnClickListener {
            try {
                binding.camera.capture()
            } catch (e: Exception) {
                Toast.makeText(this, "ERROR: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("CameraError", "Error capturing photo", e)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (REQUEST_CAMERA_CODE == requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCamera()
            } else {
                Toast.makeText(this, "App needs camera in order to work.", Toast.LENGTH_LONG).show()
                requestCameraPermissions()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (canUseCamera()) {
            camera.start()
        }
    }

    override fun onPause() {
        if (canUseCamera()) {
            camera.stop()
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (canUseCamera()) {
            camera.destroy()
        }
        super.onDestroy()
    }

    private fun canUseCamera() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val REQUEST_CAMERA_CODE = 1
    }
}




