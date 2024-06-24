package com.example.cineease.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Snack(
    val id: String = UUID.randomUUID().toString().take(10),
    val namaPaket: String? = null,
    val isiPaket: List<String>? = null,
    val harga: Double? = null
): Parcelable
