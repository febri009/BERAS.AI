package com.example.berasai


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.priyankvasa.android.cameraviewex.CameraView
import com.priyankvasa.android.cameraviewex.Modes

class KameraActivity : AppCompatActivity() {

    private lateinit var classifier: Klasifikasi

    private lateinit var resultbar: TextView
    private lateinit var cameraView: CameraView
    private lateinit var capturePhoto: CardView


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kamera)

        resultbar = findViewById(R.id.tvHasil1)
        cameraView = findViewById(R.id.camera)
        capturePhoto = findViewById(R.id.capture_photo)

        classifier = Klasifikasi(assets)

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
        cameraView.addPictureTakenListener {
            AsyncTask.execute {
                val recognitions = classifier.recognize(it.data)
                val txt = recognitions.joinToString(separator = "\n\n")
                runOnUiThread {
                    resultbar.text = txt
                }
            }
        }

        capturePhoto.setOnClickListener {
            cameraView.capture()
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
            cameraView.start()
        }
    }

    override fun onPause() {
        if (canUseCamera()) {
            cameraView.stop()
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (canUseCamera()) {
            cameraView.destroy()
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





