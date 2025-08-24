package com.example.shopapp.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.R
import com.example.shopapp.repo.Repository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CartActivity : ComponentActivity() {
    private val repo by lazy { Repository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val list = findViewById<RecyclerView>(R.id.recycler)
        val total = findViewById<TextView>(R.id.total)
        val adapter = CartAdapter()
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        lifecycleScope.launch {
            combine(repo.observeCart(), repo.observeProducts()) { cart, products ->
                val items = cart.mapNotNull { ci ->
                    val p = products.find { it.id == ci.productId } ?: return@mapNotNull null
                    CartRow(p.name, ci.quantity, p.price * ci.quantity)
                }
            items to items.sumOf { it.sum }
            }.collect { (rows, sum) ->
                adapter.submitList(rows)
                total.text = "Итого: ${sum} ₽"
            }
        }
    }
}

data class CartRow(val name: String, val qty: Int, val sum: Int)
