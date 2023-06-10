package com.example.berasai.ui.home

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.berasai.data.model.DataTengkulaks
import com.example.berasai.databinding.ListKontenBinding

class HomeAdapter(private val listHome: List<DataTengkulaks>): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    class ViewHolder(var binding: ListKontenBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListKontenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listHome.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listTengkulak = listHome[position]

        with(holder.binding) {
            tvName.text = listTengkulak.name
            tvAddress.text = listTengkulak.address
            btPhone.setOnClickListener {
                val phone = listTengkulak.phone
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                holder.itemView.context.startActivity(intent)
            }
        }
    }

}