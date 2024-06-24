package com.example.cineease.movie

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cineease.databinding.ActivityMovieDetailBinding
import com.example.cineease.model.Movie

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.btnBack.setOnClickListener { finish() }

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("movie", Movie::class.java)
        } else {
            intent.getParcelableExtra("movie")
        }

        data?.let {
            setupHalaman(it)
        } ?: {
            Toast.makeText(this, "Gagal memperoleh data", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupHalaman(movie: Movie) {
        movie.fotoUrl?.let { Glide.with(binding.ivMovie).load(it).into(binding.ivMovie) }
        binding.tvTitle.text = movie.judulFilm
        binding.tvOverview.text = movie.deskripsiFilm ?: "-"
        binding.btnBookTicket.setOnClickListener {
            val intent = Intent(this, SelectSeatActivity::class.java).apply {
                putExtra("movie", movie)
            }
            startActivity(intent)
        }
    }
}