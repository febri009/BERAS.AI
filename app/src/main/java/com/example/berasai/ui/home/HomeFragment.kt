package com.example.berasai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.berasai.data.model.DataTengkulaks
import com.example.berasai.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: HomeAdapter
    private lateinit var searchViewListener: SearchView.OnQueryTextListener


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //homeViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]

        adapter = HomeAdapter(emptyList())
        binding.rvListKonten.adapter = adapter

        searchViewListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    adapter.filterList(it)
                }
                return true
            }
        }

        binding.searchView.setOnQueryTextListener(searchViewListener)

        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        homeViewModel.listTengkulaks.observe(viewLifecycleOwner){listTengkulaks ->
            setDataArticles(listTengkulaks)
        }

        homeViewModel.loadHome.observe(viewLifecycleOwner){loadHome ->
            showLoading(loadHome)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvListKonten.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvListKonten.addItemDecoration(itemDecoration)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressHome.visibility = View.VISIBLE
        }else{
            binding.progressHome.visibility = View.GONE
        }

    }

    private fun setDataArticles(listArticles: List<DataTengkulaks>) {
        adapter.listHome = listArticles
        adapter.filterList("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}