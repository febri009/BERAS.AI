package com.example.berasai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.berasai.data.model.DataItem
import com.example.berasai.databinding.ActivityDetailKontenBinding

class DetailKontenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKontenBinding
    private lateinit var viewModel: DetailKontenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKontenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailKontenViewModel::class.java]

        val articleTitle = intent.getStringExtra(EXTRA_ARTICLE)
        if (articleTitle != null) {
            viewModel.getDetail(articleTitle)
        }

        viewModel.detailArticle.observe(this) { detailArticle ->
            setDetailArticles(detailArticle)
        }
    }

    private fun setDetailArticles(detailArticle: DataItem) {
        Glide.with(this@DetailKontenActivity)
            .load(detailArticle.imageUrl)
            .into(binding.ivThumbnail)
        binding.apply {
            tvJudul.text = detailArticle.title
            tvPenulis.text = detailArticle.author
            //tvTanggal.text = detailArticle.createdAt.toString()
            tvIsi.text = detailArticle.content
        }
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}

