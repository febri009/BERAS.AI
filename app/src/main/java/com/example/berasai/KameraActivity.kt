package com.example.berasai


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.priyankvasa.android.cameraviewex.CameraView

class KameraActivity : AppCompatActivity() {

    private lateinit var classifier: KlasifikasiDariKamera

    private lateinit var result: TextView
    private lateinit var camera: CameraView
    private lateinit var capture: CardView


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kamera)

        result = findViewById(R.id.tvHasil1)
        camera = findViewById(R.id.camera)
        capture = findViewById(R.id.capture_photo)

        classifier = KlasifikasiDariKamera(assets)

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
            requestCameraCode
        )
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun setupCamera() {
        camera.addPictureTakenListener {
            AsyncTask.execute {
                val recognitions = classifier.recognize(it.data)
                val txt = recognitions.joinToString(separator = "\n\n")
                runOnUiThread {
                    result.text = txt
                }
            }
        }

        capture.setOnClickListener {
            camera.capture()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCameraCode == requestCode) {
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
        if (hasCameraPermission()) {
            camera.start()
        }
    }

    override fun onPause() {
        if (hasCameraPermission()) {
            camera.stop()
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (hasCameraPermission()) {
            camera.destroy()
        }
        super.onDestroy()
    }

    private fun hasCameraPermission() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val requestCameraCode = 1
    }
}





