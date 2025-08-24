package com.example.shopapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name")
    fun observeAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: Long): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(products: List<Product>)

    @Update
    suspend fun update(product: Product)

    @Query("UPDATE products SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Long, favorite: Boolean)

    @Query("SELECT * FROM products WHERE isFavorite = 1")
    fun observeFavorites(): Flow<List<Product>>

    // Cart
    @Query("SELECT * FROM cart_items")
    fun observeCart(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCartItem(item: CartItem)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun removeFromCart(productId: Long)

    @Query("UPDATE cart_items SET quantity = quantity + :delta WHERE productId = :productId")
    suspend fun changeQuantity(productId: Long, delta: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
