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
import com.example.cineease.databinding.ItemPenjualanSnackBinding
import com.example.cineease.model.MovieOrder
import java.text.NumberFormat
import java.util.Locale

class PenjualanFilmAdapter: Adapter<PenjualanFilmAdapter.SnackViewHolder>() {
    inner class SnackViewHolder(private val binding: ItemPenjualanSnackBinding): ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(movie: MovieOrder) {
            with(binding) {
                tvPaket.text = movie.movie!!.judulFilm
                tvJumlah.text = "Jumlah: ${movie.seat!!.size} Tiket"
                tvHarga.text = "Harga: " + movie.movie.hargaTiket.toRupiah()
                tvTotal.text = "Total: ${movie.totalPrice.toRupiah()}"
                tvPaketItem.text = movie.seat.joinToString(",")
                Glide.with(ivFotoProduk).load(movie.movie.fotoUrl).into(ivFotoProduk)
            }
        }
    }

    private fun Double?.toRupiah() = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this ?: 0.0)

    private val diffUtil = object : DiffUtil.ItemCallback<MovieOrder>() {
        override fun areItemsTheSame(oldItem: MovieOrder, newItem: MovieOrder): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: MovieOrder, newItem: MovieOrder): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnackViewHolder =
        SnackViewHolder(
            ItemPenjualanSnackBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: SnackViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Lihat Struk")

            builder.setPositiveButton("Ya") { dialog, _ ->
                onReceipt?.invoke(item)
                dialog.dismiss()
            }

            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
            true
        }
    }

    var onReceipt: ((MovieOrder) -> Unit)? = null
}