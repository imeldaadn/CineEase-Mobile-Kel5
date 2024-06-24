package com.example.cineease.snack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cineease.databinding.FragmentSnackBinding

class SnackFragment : Fragment() {

    private lateinit var binding: FragmentSnackBinding
    private val theaterAdapter by lazy { TheaterAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSnackBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listTheater = listOf(
            "Ciputra World",
            "Delta",
            "Galaxy Mall",
            "Grand City",
            "Pakuwon City",
            "Pakuwon Mall",
            "Trans Icon Mall",
            "Transmart Rungkut"
        )

        binding.rvTheater.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTheater.adapter = theaterAdapter
        theaterAdapter.differ.submitList(listTheater)
    }
}