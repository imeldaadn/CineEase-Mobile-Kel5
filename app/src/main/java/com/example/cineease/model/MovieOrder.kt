package com.example.cineease.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
data class MovieOrder(
    val id: String = UUID.randomUUID().toString().take(10),
    val movie: Movie? = null,
    val seat: List<String>? = null,
    val totalPrice: Double? = null,
    val metodePembayaran: String? = null,
    val createdAt: Date = Date()
): Parcelable
