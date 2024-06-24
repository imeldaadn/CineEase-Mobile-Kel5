package com.example.cineease.snack

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cineease.BookSuccessActivity
import com.example.cineease.databinding.ActivityPaymentMethodBinding
import com.example.cineease.model.SnackOrder
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class SnackPaymentMethod : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentMethodBinding
    private var snackOrder: SnackOrder? = null

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
            intent.getParcelableExtra("snackOrder", SnackOrder::class.java)
        } else {
            intent.getParcelableExtra("snackOrder")
        }

        data?.let {
            snackOrder = it
            binding.tvMovieTitle.visibility = View.GONE
            binding.tvLokasi.text = "${intent.getStringExtra("theater") ?: "-"} || ${
                SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(
                    Date()
                )
            }"

            binding.tvJumlahTiket.text = "${it.jumlah} Menu"
            binding.listTiket.text = it.totalPrice?.toRupiah() + " x ${it.jumlah}"
            binding.tvKursi.text =
                "${it.snack?.namaPaket}\n${it.snack?.isiPaket?.joinToString(" + ")}"
            binding.tvTotalSeats.text = "${it.jumlah} Menu"
            binding.tvTotalPrice.text = (it.totalPrice!! * 0.5).toRupiah()

            binding.cvGopay.setOnClickListener { setDialog() }
            binding.cvCash.setOnClickListener { setDialog() }

            binding.btnBookTicket.setOnClickListener {
                payment()
            }
        }

        if (data == null) {
            Toast.makeText(this, "Gagal memperoleh data", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun payment() {
        val firestore = Firebase.firestore
        firestore.collection("snackOrder").document(snackOrder!!.id).set(snackOrder!!)
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menambah produk", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                val intent = Intent(this, BookSuccessActivity::class.java).apply {
                    putExtra("orderType", "snack")
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
                snackOrder = snackOrder!!.copy(metodePembayaran = "GoPay")
                binding.cvGopay.visibility = View.VISIBLE
                binding.cvCash.visibility = View.INVISIBLE
            }

            "Cash" -> {
                snackOrder = snackOrder!!.copy(metodePembayaran = "Cash")
                binding.cvGopay.visibility = View.INVISIBLE
                binding.cvCash.visibility = View.VISIBLE
            }
        }
    }

    private fun Double.toRupiah() =
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this)
}