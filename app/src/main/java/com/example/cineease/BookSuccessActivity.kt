package com.example.cineease

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.cineease.databinding.ActivityBookSuccessBinding

class BookSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookSuccessBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("orderType")?.let {
            when (it) {
                "movie" -> {
                    binding.tvBookDesc.text = getString(R.string.book_success, intent.getStringExtra("movieName") ?: "-")
                }
                "snack" -> {
                    binding.tvBook.text = "Pemesanan\nBerhasil"
                    binding.tvBookDesc.visibility = View.INVISIBLE
                }
            }
        }

        binding.btnBackToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@BookSuccessActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }
        })
    }
}