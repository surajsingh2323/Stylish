package com.example.stylish.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stylish.data.dto.Product
import com.example.stylish.data.local.converter.StringListConverter
import com.example.stylish.data.local.dao.WishlistDao

@Database(
    entities = [Product::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class EcoMartDatabase : RoomDatabase(){
    abstract fun wishlistDao(): WishlistDao
}