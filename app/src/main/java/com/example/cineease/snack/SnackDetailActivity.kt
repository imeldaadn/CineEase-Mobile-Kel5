package com.example.cineease.snack

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cineease.databinding.ActivitySnackDetailBinding
import com.example.cineease.model.Snack
import com.example.cineease.model.SnackOrder

@Suppress("DEPRECATION")
class SnackDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySnackDetailBinding
    private val snackAdapter by lazy { SnackPickAdapter() }
    private var jumlahBeli = 0
    private var selectedSnack: Snack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnackDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
    }

    private fun getData() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("snack", Snack::class.java)
        } else {
            intent.getParcelableExtra("snack")
        }

        data?.let {
            with(binding) {
                selectedSnack = it
                rvSnack.layoutManager = LinearLayoutManager(this@SnackDetailActivity)
                rvSnack.adapter = snackAdapter
                snackAdapter.differ.submitList(listOf(it))
                snackAdapter.onMinusClick = {
                    if (jumlahBeli != 0) jumlahBeli -= 1
                }
                snackAdapter.onPlusClick = {
                    jumlahBeli += 1
                }

                tvPaketDetail.text = it.isiPaket?.joinToString("\n")
                btnLanjutkan.setOnClickListener {
                    if (jumlahBeli != 0) lanjutkanPembayaran()
                    else Toast.makeText(
                        this@SnackDetailActivity,
                        "Jumlah minimal pembelian adalah 1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun lanjutkanPembayaran() {
        val snackOrder = SnackOrder(
            snack = selectedSnack,
            jumlah = jumlahBeli,
            totalPrice = selectedSnack!!.harga!! * jumlahBeli,
            note = binding.etCatatan.text.toString().ifEmpty { "-" },
            metodePembayaran = "Cash"
        )
        val intent = Intent(this, SnackPaymentMethod::class.java).apply {
            putExtra("snackOrder", snackOrder)
            putExtra("theater", intent.getStringExtra("theater") ?: "-")
        }
        startActivity(intent)
    }
}