package com.example.cineease.movie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cineease.databinding.FragmentHomeBinding
import com.example.cineease.model.Movie
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val movieAdapter by lazy { MovieAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView()
        getData()
    }

    private fun getData() {
        val firestore = Firebase.firestore
        firestore.collection("movie").get()
            .addOnSuccessListener {
                val movies = it.toObjects(Movie::class.java)
                movieAdapter.differ.submitList(movies)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal mendapat data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun recyclerView() {
        with(binding) {
            rvUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
            rvUpcoming.adapter = movieAdapter

            rvWeekendOffer.layoutManager = GridLayoutManager(requireContext(), 2)
            rvWeekendOffer.adapter = movieAdapter

            movieAdapter.onClick = {
                val intent = Intent(requireContext(), MovieDetailActivity::class.java).apply {
                    putExtra("movie", it)
                }
                startActivity(intent)
            }
        }
    }

}