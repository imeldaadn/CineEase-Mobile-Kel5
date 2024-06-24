package com.example.cineease.pos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cineease.databinding.ActivityLaporanPenjualanBinding
import com.example.cineease.pos.film.PenjualanFilmActivity
import com.example.cineease.pos.snack.PenjualanSnackActivity

class LaporanPenjualanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaporanPenjualanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporanPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.btnBack.setOnClickListener {
            finish()
        }
        binding.btnSnack.setOnClickListener {
            val intent = Intent(this, PenjualanSnackActivity::class.java)
            startActivity(intent)
        }
        binding.btnFilm.setOnClickListener {
            val intent = Intent(this, PenjualanFilmActivity::class.java)
            startActivity(intent)
        }
    }
}