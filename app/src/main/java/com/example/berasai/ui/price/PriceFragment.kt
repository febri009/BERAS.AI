package com.example.berasai.ui.price

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.berasai.data.model.DataPrices
import com.example.berasai.databinding.FragmentPriceBinding

class PriceFragment : Fragment() {

    private var _binding: FragmentPriceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var priceViewModel : PriceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPriceBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        priceViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[PriceViewModel::class.java]

        priceViewModel.listDataPrices.observe(viewLifecycleOwner){listDataPrices ->
            setDataPrices(listDataPrices)
        }

        priceViewModel.loadPrice.observe(viewLifecycleOwner){loadPrice ->
            showLoading(loadPrice)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvListKonten.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvListKonten.addItemDecoration(itemDecoration)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressPrice.visibility = View.VISIBLE
        }else{
            binding.progressPrice.visibility = View.GONE
        }

    }

    private fun setDataPrices(listDataPrices: List<DataPrices>) {
        val dataPrices = ArrayList<DataPrices>()
        for (price in listDataPrices){
            dataPrices.clear()
            dataPrices.addAll(listDataPrices)
        }

        val adapter = PriceAdapter(dataPrices)
        binding.rvListKonten.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}