package com.example.shopapp.repo

import android.content.Context
import com.example.shopapp.data.AppDatabase
import com.example.shopapp.data.CartItem
import com.example.shopapp.data.Product
import kotlinx.coroutines.flow.Flow

class Repository(context: Context) {
    private val dao = AppDatabase.get(context).productDao()

    fun observeProducts(): Flow<List<Product>> = dao.observeAllProducts()
    fun observeFavorites(): Flow<List<Product>> = dao.observeFavorites()
    fun observeCart(): Flow<List<CartItem>> = dao.observeCart()

    suspend fun getProduct(id: Long) = dao.getById(id)

    suspend fun toggleFavorite(product: Product) {
        dao.setFavorite(product.id, !product.isFavorite)
    }

    suspend fun addToCart(productId: Long) {
        dao.upsertCartItem(CartItem(productId = productId, quantity = 1))
    }

    suspend fun changeCart(productId: Long, delta: Int) {
        dao.changeQuantity(productId, delta)
    }

    suspend fun removeFromCart(productId: Long) = dao.removeFromCart(productId)
}
