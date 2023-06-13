package com.example.berasai.ui.scan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.berasai.detection.GaleriActivity
import com.example.berasai.detection.KameraActivity
import com.example.berasai.databinding.FragmentScanBinding

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonCamera.setOnClickListener {
            val intent = Intent(requireContext(), KameraActivity::class.java)
            startActivity(intent)
        }

        binding.buttonGallery.setOnClickListener {
            val intent = Intent(requireContext(), GaleriActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}