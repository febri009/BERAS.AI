package com.example.berasai.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.berasai.DetailKontenActivity
import com.example.berasai.data.model.DataItem
import com.example.berasai.databinding.ListKontenBinding

class HomeAdapter(private val listHome: List<DataItem>): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    class ViewHolder(var binding: ListKontenBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListKontenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listHome.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listArticles = listHome[position]

        with(holder.binding) {
            Glide.with(root.context)
                .load(listArticles.imageUrl)
                .circleCrop()
                .centerCrop()
                .into(ivGambarKonten)
            tvJudulKonten.text = listArticles.title
            tvIsiKonten.text = listArticles.content
            tvNamaPenulis.text = listArticles.author
            root.setOnClickListener {
                val intent = Intent(root.context, DetailKontenActivity::class.java)
                intent.putExtra(DetailKontenActivity.EXTRA_ARTICLE, listArticles.title)
                root.context.startActivity(intent)
            }
        }
    }
}