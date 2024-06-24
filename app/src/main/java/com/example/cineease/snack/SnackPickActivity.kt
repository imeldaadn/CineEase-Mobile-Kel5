package com.example.cineease.snack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cineease.databinding.ActivitySnackPickBinding
import com.example.cineease.model.Snack
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class SnackPickActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySnackPickBinding
    private lateinit var firestore: FirebaseFirestore
    private val snackAdapter by lazy { SnackPickAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnackPickBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = Firebase.firestore

        binding.btnBack.btnBack.setOnClickListener {
            finish()
        }
        binding.rvSnack.layoutManager = LinearLayoutManager(this)
        binding.rvSnack.adapter = snackAdapter
        snackAdapter.onClick = {
            val intent = Intent(this, SnackDetailActivity::class.java).apply {
                putExtra("snack", it)
                putExtra("theater", intent.getStringExtra("theater") ?: "-")
            }
            startActivity(intent)
        }

        getData()
    }

    private fun getData() {
        firestore.collection("snack").get()
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                val snacks = it.toObjects(Snack::class.java)
                snackAdapter.differ.submitList(snacks)
            }
    }
}