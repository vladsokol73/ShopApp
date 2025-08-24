package com.example.shopapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.R

class CartAdapter : ListAdapter<CartRow, CartAdapter.Holder>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val qty: TextView = itemView.findViewById(R.id.qty)
        private val sum: TextView = itemView.findViewById(R.id.sum)
        fun bind(row: CartRow) {
            title.text = row.name
            qty.text = "×${row.qty}"
            sum.text = "${row.sum} ₽"
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CartRow>() {
            override fun areItemsTheSame(oldItem: CartRow, newItem: CartRow) = oldItem.name == newItem.name
            override fun areContentsTheSame(oldItem: CartRow, newItem: CartRow) = oldItem == newItem
        }
    }
}
