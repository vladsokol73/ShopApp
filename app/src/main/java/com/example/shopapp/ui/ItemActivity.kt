package com.example.shopapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.shopapp.R
import com.example.shopapp.repo.Repository
import kotlinx.coroutines.launch

class ItemActivity : ComponentActivity() {
    private val repo by lazy { Repository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val id = intent.getLongExtra("item_id", -1)
        val title = findViewById<TextView>(R.id.title)
        val price = findViewById<TextView>(R.id.price)
        val fav = findViewById<Button>(R.id.toggle_fav)
        val cart = findViewById<Button>(R.id.add_cart)

        lifecycleScope.launch {
            val p = repo.getProduct(id) ?: return@launch
            title.text = p.name
            price.text = "${p.price} ₽"
            fav.text = if (p.isFavorite) "Убрать из избранного" else "В избранное"

            fav.setOnClickListener {
                lifecycleScope.launch { repo.toggleFavorite(p) }
            }
            cart.setOnClickListener {
                lifecycleScope.launch { repo.addToCart(p.id) }
            }
        }
    }
}
