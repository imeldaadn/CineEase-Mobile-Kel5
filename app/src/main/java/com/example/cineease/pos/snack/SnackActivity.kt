package com.example.cineease.pos.snack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cineease.databinding.ActivitySnackBinding
import com.example.cineease.model.Snack
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class SnackActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySnackBinding
    private lateinit var firestore: FirebaseFirestore
    private val snackAdapter by lazy { SnackAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = Firebase.firestore

        binding.btnBack.btnBack.setOnClickListener {
            finish()
        }

        binding.btnTambah.setOnClickListener {
            val intent = Intent(this, AddSnackActivity::class.java)
            startActivity(intent)
        }

        binding.rvSnack.layoutManager = LinearLayoutManager(this)
        binding.rvSnack.adapter = snackAdapter
        snackAdapter.onDelete = {
            deleteData(it.id)
        }
        snackAdapter.onClick = {
            val intent = Intent(this, AddSnackActivity::class.java).apply {
                putExtra("snack", it)
            }
            startActivity(intent)
        }

        getData()
    }

    private fun deleteData(id: String) {
        firestore.collection("snack").document(id).delete()
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

    override fun onResume() {
        super.onResume()
        getData()
    }
}