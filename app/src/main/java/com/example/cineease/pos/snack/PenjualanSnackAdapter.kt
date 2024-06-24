package com.example.cineease.pos.snack

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cineease.databinding.ItemPenjualanSnackBinding
import com.example.cineease.model.SnackOrder
import java.text.NumberFormat
import java.util.Locale

class PenjualanSnackAdapter: Adapter<PenjualanSnackAdapter.SnackViewHolder>() {
    inner class SnackViewHolder(private val binding: ItemPenjualanSnackBinding): ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(snack: SnackOrder) {
            with(binding) {
                tvPaket.text = snack.snack!!.namaPaket
                tvJumlah.text = "Jumlah: ${snack.jumlah}"
                tvHarga.text = "Harga: " + snack.snack.harga.toRupiah()
                tvTotal.text = "Total: ${snack.totalPrice.toRupiah()}"
                tvPaketItem.text = snack.snack.isiPaket?.joinToString(" + ")
            }
        }
    }

    private fun Double?.toRupiah() = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this ?: 0.0)

    private val diffUtil = object : DiffUtil.ItemCallback<SnackOrder>() {
        override fun areItemsTheSame(oldItem: SnackOrder, newItem: SnackOrder): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: SnackOrder, newItem: SnackOrder): Boolean =
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

    var onReceipt: ((SnackOrder) -> Unit)? = null
}