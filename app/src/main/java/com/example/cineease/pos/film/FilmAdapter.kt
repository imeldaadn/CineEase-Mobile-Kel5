package com.example.cineease.pos.film

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cineease.databinding.ItemMovieBinding
import com.example.cineease.model.Movie
import java.text.NumberFormat
import java.util.Locale

class FilmAdapter: Adapter<FilmAdapter.FilmViewHolder>() {
    inner class FilmViewHolder(private val binding: ItemMovieBinding): ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) {
            with(binding) {
                tvPaket.text = movie.judulFilm
                tvHarga.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(movie.hargaTiket ?: 0.0)
                tvStok.text = "Stok: ${movie.stokFilm}"
                movie.fotoUrl?.let { Glide.with(ivFotoProduk).load(it).into(ivFotoProduk) }
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder =
        FilmViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Konfirmasi Hapus")
            builder.setMessage("Apakah Anda yakin ingin menghapus menu ini?")

            builder.setPositiveButton("Ya") { dialog, _ ->
                onDelete?.invoke(item)
                dialog.dismiss()
            }

            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
            true
        }

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    var onDelete: ((Movie) -> Unit)? = null
    var onClick: ((Movie) -> Unit)? = null
}