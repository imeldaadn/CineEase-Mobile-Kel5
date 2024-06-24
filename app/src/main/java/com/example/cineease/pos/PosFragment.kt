package com.example.cineease.pos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cineease.R
import com.example.cineease.databinding.FragmentPosBinding

class PosFragment : Fragment() {

    private lateinit var binding: FragmentPosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPosBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnKelolaInventaris.setOnClickListener {
            val intent = Intent(requireContext(), KelolaInventarisActivity::class.java)
            startActivity(intent)
        }
        binding.btnLaporanPenjualan.setOnClickListener {
            val intent = Intent(requireContext(), LaporanPenjualanActivity::class.java)
            startActivity(intent)
        }
    }
}