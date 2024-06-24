package com.example.cineease

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cineease.databinding.ActivityPaymentMethodBinding
import com.example.cineease.model.MovieOrder
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentMethod : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentMethodBinding
    private var movieOrder: MovieOrder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.btnBack.setOnClickListener { finish() }
        getData()
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("movieOrder", MovieOrder::class.java)
        } else {
            intent.getParcelableExtra("movieOrder")
        }

        data?.let {
            movieOrder = it
            binding.tvMovieTitle.text = it.movie!!.judulFilm
            binding.tvLokasi.text = "AKZ Cineplex, Bandung || ${
                SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(
                    Date()
                )
            }"
            binding.tvJumlahTiket.text = "${it.seat!!.size} TIKET"
            binding.listTiket.text = it.seat.joinToString(",")
            binding.hargaTiket.text = "${it.movie.hargaTiket?.toRupiah()} x ${it.seat.size}"
            binding.tvTotalPrice.text = (it.movie.hargaTiket!! * it.seat.size * 0.5).toRupiah()
            binding.tvTotalSeats.text = "${it.seat.size} Seats"

            binding.cvCash.setOnClickListener { setDialog() }
            binding.cvGopay.setOnClickListener { setDialog() }

            binding.btnBookTicket.setOnClickListener { payment() }
        }

        if (data == null) {
            Toast.makeText(this, "Gagal memperoleh data", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun payment() {
        val firestore = Firebase.firestore
        firestore.collection("movieOrder").document(movieOrder!!.id).set(movieOrder!!)
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menambah produk", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                val intent = Intent(this, BookSuccessActivity::class.java).apply {
                    putExtra("orderType", "movie")
                    putExtra("movieName", movieOrder!!.movie!!.judulFilm)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
    }

    private fun setDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Pembayaran")

        val methods = listOf("Cash", "Gopay").toTypedArray()
        builder.setItems(methods) { _, which ->
            val method = methods[which]
            setPayment(method)
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun setPayment(method: String) {
        when (method) {
            "Gopay" -> {
                movieOrder = movieOrder!!.copy(metodePembayaran = "GoPay")
                binding.cvGopay.visibility = View.VISIBLE
                binding.cvCash.visibility = View.INVISIBLE
            }

            "Cash" -> {
                movieOrder = movieOrder!!.copy(metodePembayaran = "Cash")
                binding.cvGopay.visibility = View.INVISIBLE
                binding.cvCash.visibility = View.VISIBLE
            }
        }
    }

    private fun Double.toRupiah() =
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this)
}