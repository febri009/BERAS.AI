package com.example.berasai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.berasai.data.model.DataItem
import com.example.berasai.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

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
        homeViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)

        homeViewModel.listArticles.observe(viewLifecycleOwner){listArticles ->
            setDataArticles(listArticles)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvListKonten.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvListKonten.addItemDecoration(itemDecoration)
    }

    private fun setDataArticles(listArticles: List<DataItem>) {
        val listUser = ArrayList<DataItem>()
        for (user in listArticles) {
            listUser.clear()
            listUser.addAll(listArticles)
        }
        val adapter = HomeAdapter(listUser)

        binding.rvListKonten.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}