package com.example.cineease.pos.snack

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cineease.ReceiptActivity
import com.example.cineease.databinding.ActivityPenjualanSnackBinding
import com.example.cineease.model.SnackOrder
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PenjualanSnackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPenjualanSnackBinding
    private var snackOrder: List<SnackOrder>? = null
    private val today = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())
    private val snackAdapter by lazy { PenjualanSnackAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenjualanSnackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvSnack.layoutManager = LinearLayoutManager(this)
        binding.rvSnack.adapter = snackAdapter
        snackAdapter.onReceipt = {
            val intent = Intent(this, ReceiptActivity::class.java).apply {
                putExtra("type", "snack")
                putExtra("snack", it)
            }
            startActivity(intent)
        }

        binding.etPeriode.setOnClickListener { getPeriode() }
        binding.etPeriode.setText(today)
        getData()
    }

    private fun getPeriode() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this@PenjualanSnackActivity, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                binding.etPeriode.setText(selectedDate.time.toTodayString())
                getData(selectedDate.time.toTodayString())
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun getData(date: String? = null) {
        val firestore = Firebase.firestore
        firestore.collection("snackOrder").get().addOnSuccessListener {
                val items = it.toObjects(SnackOrder::class.java)
                snackOrder = items
                snackAdapter.differ.submitList(items.filter { order ->
                    order.createdAt.toTodayString() == (date ?: today)
                })
            }
    }

    private fun Date.toTodayString() =
        SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(this)
}