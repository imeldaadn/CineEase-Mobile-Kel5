package com.example.cineease.snack

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cineease.databinding.ItemTheaterBinding

class TheaterAdapter: Adapter<TheaterAdapter.FilmViewHolder>() {
    inner class FilmViewHolder(private val binding: ItemTheaterBinding): ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(theater: String) {
            with(binding) {
                btnTheater.text = theater
                btnTheater.setOnClickListener {
                    itemView.context.startActivity(
                        Intent(itemView.context, SnackPickActivity::class.java).apply {
                            putExtra("theater", theater)
                        }
                    )
                }
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder =
        FilmViewHolder(
            ItemTheaterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }
}