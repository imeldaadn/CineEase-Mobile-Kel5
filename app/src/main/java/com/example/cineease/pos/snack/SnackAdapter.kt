package com.example.cineease.pos.snack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cineease.databinding.ItemSnackBinding
import com.example.cineease.model.Snack
import java.text.NumberFormat
import java.util.Locale

class SnackAdapter: Adapter<SnackAdapter.SnackViewHolder>() {
    inner class SnackViewHolder(private val binding: ItemSnackBinding): ViewHolder(binding.root) {
        fun bind(snack: Snack) {
            with(binding) {
                tvPaket.text = snack.namaPaket
                tvHarga.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(snack.harga ?: 0.0)
                tvPaketItem.text = snack.isiPaket?.joinToString(" + ")
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Snack>() {
        override fun areItemsTheSame(oldItem: Snack, newItem: Snack): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Snack, newItem: Snack): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnackViewHolder =
        SnackViewHolder(
            ItemSnackBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: SnackViewHolder, position: Int) {
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

    var onDelete: ((Snack) -> Unit)? = null
    var onClick: ((Snack) -> Unit)? = null
}