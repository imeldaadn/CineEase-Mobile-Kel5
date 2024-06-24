package com.example.cineease.pos.snack

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cineease.databinding.ActivityAddSnackBinding
import com.example.cineease.model.Snack
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

@Suppress("DEPRECATION")
class AddSnackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSnackBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSnackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = Firebase.firestore

        cekUpdate()
        btnBinding()
    }

    @SuppressLint("SetTextI18n")
    private fun cekUpdate() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("snack", Snack::class.java)
        } else {
            intent.getParcelableExtra("snack")
        }

        data?.let {
            binding.etNamaPaket.setText(it.namaPaket)
            binding.etHarga.setText(it.harga.toString())

            val isiPaket = it.isiPaket?.joinToString("\n")
            binding.etIsiPaket.setText(isiPaket)

            binding.btnTambah.text = "Update"
        }
    }

    private fun btnBinding() {
        with(binding) {
            btnBack.btnBack.setOnClickListener {
                finish()
            }

            btnTambah.setOnClickListener {
                if (!isInputValid()) {
                    Toast.makeText(this@AddSnackActivity, "Harap isi semua bagian", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val snack = Snack(
                    namaPaket = etNamaPaket.text.toString(),
                    isiPaket = etIsiPaket.text.toString().lines().toMutableList(),
                    harga = etHarga.text.toString().toDoubleOrNull()
                )
                saveData(snack)
            }
        }
    }

    private fun saveData(snack: Snack) {
        binding.progressBar.visibility = View.VISIBLE
        firestore.collection("snack").document(snack.id).set(snack)
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Terjadi error", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Berhasil menyimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun isInputValid(): Boolean {
        with(binding) {
            when {
                etNamaPaket.text.toString().isEmpty() -> return false
                etIsiPaket.text.toString().isEmpty() -> return false
                etHarga.text.toString().isEmpty() -> return false
                else -> return true
            }
        }
    }
}