package com.example.cineease.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Movie(
    val id: String = UUID.randomUUID().toString().take(10),
    val judulFilm: String? = null,
    val ratingFilm: Double? = null,
    val deskripsiFilm: String? = null,
    val stokFilm: Long? = null,
    val hargaTiket: Double? = null,
    val fotoUrl: String? = null
): Parcelable
