package com.example.shopapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.R
import com.example.shopapp.data.Product
import com.example.shopapp.repo.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repo: Repository) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        viewModelScope.launch {
            repo.observeProducts().collect { _products.value = it }
        }
    }
}

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(Repository(this@MainActivity)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = findViewById<RecyclerView>(R.id.recycler)
        val adapter = ProductAdapter(
            onItemClick = { openItem(it.id) }
        )
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        lifecycleScope.launch {
            viewModel.products.collect { adapter.submitList(it) }
        }

        findViewById<android.view.View>(R.id.open_cart).setOnClickListener { openCart() }
    }

    private fun openItem(id: Long) {
        val i = Intent(this, ItemActivity::class.java)
        i.putExtra("item_id", id)
        startActivity(i)
    }

    private fun openCart() {
        startActivity(Intent(this, CartActivity::class.java))
    }
}
