package com.example.cineease.snack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cineease.databinding.ItemSnackPickBinding
import com.example.cineease.model.Snack
import java.text.NumberFormat
import java.util.Locale

class SnackPickAdapter : Adapter<SnackPickAdapter.SnackViewHolder>() {
    inner class SnackViewHolder(private val binding: ItemSnackPickBinding) :
        ViewHolder(binding.root) {
        fun bind(snack: Snack) {
            with(binding) {
                tvPaket.text = snack.namaPaket
                tvHarga.text =
                    NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(snack.harga ?: 0.0)
                tvPaketItem.text = snack.isiPaket?.joinToString(" + ")

                var jumlahBeli = 0
                btnDecrease.setOnClickListener {
                    if (jumlahBeli != 0) {
                        onMinusClick?.invoke(snack)
                        jumlahBeli -= 1
                        tvJumlahBeli.text = jumlahBeli.toString()
                    }
                }
                btnIncrease.setOnClickListener {
                    onPlusClick?.invoke(snack)
                    jumlahBeli += 1
                    tvJumlahBeli.text = jumlahBeli.toString()
                }
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
            ItemSnackPickBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: SnackViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    var onClick: ((Snack) -> Unit)? = null
    var onPlusClick: ((Snack) -> Unit)? = null
    var onMinusClick: ((Snack) -> Unit)? = null
}