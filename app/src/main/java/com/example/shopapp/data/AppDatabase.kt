package com.example.shopapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Product::class, CartItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "shop.db"
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Seed initial data
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = get(context).productDao()
                        dao.upsertAll(sampleProducts())
                    }
                }
            }).build()
            INSTANCE = instance
            instance
        }

        private fun sampleProducts(): List<Product> = listOf(
            Product(name = "Кружка Latte 350 мл", price = 499),
            Product(name = "Термокружка Steel 500 мл", price = 1299),
            Product(name = "Бутылка Aqua 750 мл", price = 799),
            Product(name = "Кружка Classic 300 мл", price = 399),
            Product(name = "Термос Urban 750 мл", price = 1699),
            Product(name = "Бутылка Sport 600 мл", price = 699)
        )
    }
}
