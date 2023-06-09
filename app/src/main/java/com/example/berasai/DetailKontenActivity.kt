package com.example.berasai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.berasai.data.model.DataItem
import com.example.berasai.databinding.ActivityDetailKontenBinding

class DetailKontenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKontenBinding
    private lateinit var viewModel:DetailKontenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKontenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailKontenViewModel::class.java)
        
        viewModel.listDetailArticles.observe(this){listDetailArticles ->
            setDetailArticles(listDetailArticles)
        }
    }

    private fun setDetailArticles(listDetailArticles: DataItem) {
        Glide.with(this@DetailKontenActivity)
            .load(listDetailArticles.articleImages)
            .into(binding.ivThumbnail)
        binding.apply {
            tvJudul.text = listDetailArticles.judul
            tvPenulis.text = listDetailArticles.author
            tvTanggal.text = listDetailArticles.createdAt
            tvIsi.text = listDetailArticles.content
        }
    }
    companion object{
        const val EXTRA_ARTICLE = "extra_article"
    }
}