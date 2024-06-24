package com.example.cineease.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
data class SnackOrder(
    val id: String = UUID.randomUUID().toString().take(10),
    val snack: Snack? = null,
    val jumlah: Int? = null,
    val totalPrice: Double? = null,
    val note: String? = null,
    val metodePembayaran: String? = null,
    val createdAt: Date = Date()
): Parcelable
