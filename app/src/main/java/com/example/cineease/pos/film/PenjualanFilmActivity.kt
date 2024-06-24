package com.example.cineease.pos.film

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cineease.ReceiptActivity
import com.example.cineease.databinding.ActivityPenjualanFilmBinding
import com.example.cineease.model.MovieOrder
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PenjualanFilmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPenjualanFilmBinding
    private var movieOrder: List<MovieOrder>? = null
    private val today = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())
    private val movieAdapter by lazy { PenjualanFilmAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenjualanFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvSnack.layoutManager = LinearLayoutManager(this)
        binding.rvSnack.adapter = movieAdapter
        movieAdapter.onReceipt = {
            val intent = Intent(this, ReceiptActivity::class.java).apply {
                putExtra("type", "movie")
                putExtra("movie", it)
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
            this@PenjualanFilmActivity, { _, selectedYear, selectedMonth, selectedDay ->
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
        firestore.collection("movieOrder").get().addOnSuccessListener {
            val items = it.toObjects(MovieOrder::class.java)
            movieOrder = items
            movieAdapter.differ.submitList(items.filter { order ->
                order.createdAt.toTodayString() == (date ?: today)
            })
        }
    }

    private fun Date.toTodayString() =
        SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(this)
}