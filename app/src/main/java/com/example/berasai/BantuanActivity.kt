package com.example.berasai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class BantuanActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bantuan)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.title = "Bantuan"

        // Menambahkan aksi kembali (back action) saat tombol kembali pada toolbar ditekan
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}