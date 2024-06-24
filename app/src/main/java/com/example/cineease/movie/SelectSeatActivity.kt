package com.example.cineease.movie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.cineease.PaymentMethod
import com.example.cineease.R
import com.example.cineease.databinding.ActivitySelectSeatBinding
import com.example.cineease.model.Movie
import com.example.cineease.model.MovieOrder
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.Locale


class SelectSeatActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectSeatBinding
    private val selectedSeats = mutableListOf<String>()
    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectSeatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
        addSeatButtons()
    }

    private fun getData() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("movie", Movie::class.java)
        } else {
            intent.getParcelableExtra("movie")
        }

        data?.let {
            movie = it
            binding.tvMovieTitle.text = it.judulFilm
        } ?: {
            Toast.makeText(this, "Gagal memperoleh data", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnBack.btnBack.setOnClickListener { finish() }
        binding.btnPayment.setOnClickListener {
            bayar()
        }
    }

    private fun bayar() {
        if (selectedSeats.isEmpty()) {
            Toast.makeText(this, "Harap memilih kursi terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val movieOrder = MovieOrder(
            movie = movie,
            seat = selectedSeats,
            totalPrice = movie!!.hargaTiket!! * selectedSeats.size,
            metodePembayaran = "Cash"
        )
        val intent =
            Intent(this, PaymentMethod::class.java).apply { putExtra("movieOrder", movieOrder) }
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun addSeatButtons() {
        var row = 'A'

        for (i in 0 until 6) {
            for (j in 1..10) {
                val seatButton = MaterialButton(this)
                val seatNumber = "$row$j"
                seatButton.background = AppCompatResources.getDrawable(
                    this, R.drawable.seat_selector
                )
                seatButton.setOnClickListener {
                    if (selectedSeats.contains(seatNumber)) {
                        selectedSeats.remove(seatNumber)
                        seatButton.background = AppCompatResources.getDrawable(
                            this, R.drawable.seat_selector
                        )
                        binding.tvTotalSeats.text = "${selectedSeats.size} Seats"
                        binding.tvTotalPrice.text =
                            (movie!!.hargaTiket!! * selectedSeats.size).toRupiah()
                    } else {
                        selectedSeats.add(seatNumber)
                        seatButton.background = AppCompatResources.getDrawable(
                            this, R.drawable.seat_selector_active
                        )
                        binding.tvTotalSeats.text = "${selectedSeats.size} Seats"
                        binding.tvTotalPrice.text =
                            (movie!!.hargaTiket!! * selectedSeats.size).toRupiah()
                    }
                }

                val params = GridLayout.LayoutParams().apply {
                    width = 0
                    height = dpToPx(35)
                    setMargins(5, 5, 5, 5)
                    columnSpec = GridLayout.spec(j - 1, 1, 1f)
                    rowSpec = GridLayout.spec(i, 1, 1f)
                    if (j % 5 == 0 && j != 10) {
                        marginEnd = dpToPx(30)
                    }
                }
                seatButton.layoutParams = params

                binding.glSeats.addView(seatButton)
            }
            row++
        }
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
        ).toInt()
    }

    private fun Double.toRupiah() =
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this)
}