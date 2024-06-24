package com.example.cineease.pos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cineease.databinding.ActivityKelolaInventarisBinding
import com.example.cineease.pos.film.FilmActivity
import com.example.cineease.pos.snack.SnackActivity

class KelolaInventarisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKelolaInventarisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKelolaInventarisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSnack.setOnClickListener {
            val intent = Intent(this, SnackActivity::class.java)
            startActivity(intent)
        }

        binding.btnFilm.setOnClickListener {
            val intent = Intent(this, FilmActivity::class.java)
            startActivity(intent)
        }
    }
}