package com.example.cineease

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cineease.databinding.ActivityReceiptBinding
import com.example.cineease.model.MovieOrder
import com.example.cineease.model.SnackOrder
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class ReceiptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceiptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.btnBack.setOnClickListener { finish() }
        intent.getStringExtra("type")?.let {
            when (it) {
                "snack" -> { snackSetup() }
                else -> { movieSetup() }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun snackSetup() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("snack", SnackOrder::class.java)
        } else {
            intent.getParcelableExtra("snack")
        }

        data?.let {
            with(binding) {
                tvJudulFilm.text = it.snack?.namaPaket
                tvDate.text = "Date: " + it.createdAt.toDate()
                tvTime.text = "Time: " + it.createdAt.toTime()
                tvSeat.text = "Paket: " + it.snack?.isiPaket?.joinToString(" + ")
                tvPrice.text = "Price: " + it.totalPrice.toRupiah()
                btnSelesai.setOnClickListener { finish() }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun movieSetup() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("movie", MovieOrder::class.java)
        } else {
            intent.getParcelableExtra("movie")
        }

        data?.let {
            with(binding) {
                tvJudulFilm.text = it.movie?.judulFilm
                tvDate.text = "Date: " + it.createdAt.toDate()
                tvTime.text = "Time: " + it.createdAt.toTime()
                tvSeat.text = "Paket: " + it.seat?.joinToString(",")
                tvPrice.text = "Price: " + it.totalPrice.toRupiah()
                btnSelesai.setOnClickListener { finish() }
            }
        }
    }

    private fun Double?.toRupiah() = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this ?: 0.0)
    private fun Date.toDate() = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(this)
    private fun Date.toTime() = SimpleDateFormat("HH : mm", Locale("id", "ID")).format(this)
}