package com.example.berasai.ui.price

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.berasai.data.model.DataPrices
import com.example.berasai.databinding.ListHargaBinding

class PriceAdapter(private val listAdapterPrices: List<DataPrices>): RecyclerView.Adapter<PriceAdapter.ViewHolder>(){
    class ViewHolder (var binding: ListHargaBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListHargaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listAdapterPrices.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listPrices = listAdapterPrices[position]

        with(holder.binding){
            tvDKI.text = listPrices.province
            tvPrice.text = "Rp" + listPrices.price.toString()
        }
    }

}