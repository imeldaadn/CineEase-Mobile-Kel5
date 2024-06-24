package com.example.cineease.pos.film

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cineease.databinding.ActivityFilmBinding
import com.example.cineease.model.Movie
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FilmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmBinding
    private val filmAdapter by lazy { FilmAdapter() }
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = Firebase.firestore
        binding.rvMovie.layoutManager = LinearLayoutManager(this)
        binding.rvMovie.adapter = filmAdapter
        filmAdapter.onDelete = { deleteMovie(it.id) }
        filmAdapter.onClick = {
            val intent = Intent(this, AddFilmActivity::class.java).apply {
                putExtra("movie", it)
            }
            startActivity(intent)
        }

        binding.btnTambah.setOnClickListener {
            val intent = Intent(this, AddFilmActivity::class.java)
            startActivity(intent)
        }

        getData()
    }

    private fun getData() {
        firestore.collection("movie").get()
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                val movies = it.toObjects(Movie::class.java)
                filmAdapter.differ.submitList(movies)
            }
    }

    private fun deleteMovie(id: String) {
        firestore.collection("movie").document(id).delete()
        getData()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}