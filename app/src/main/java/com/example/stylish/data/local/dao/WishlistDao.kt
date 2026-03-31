package com.example.stylish.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.stylish.data.dto.Product

@Dao
interface WishlistDao {
    // Fetches all items currently stored in the wishlist table as a stream (Flow).
    // Whenever data changes, the observer automatically gets updates.
    @Query("SELECT * FROM wishlist")
    fun getAllWishlistItems(): Flow<List<Product>>

    // Fetches a specific wishlist item by product ID.
    // Returns null if the product is not found in the wishlist.
    @Query("SELECT * FROM wishlist WHERE id = :productId")
    suspend fun getWishlistItem(productId: Int): Product?

    // Inserts a new product into the wishlist.
    // If the product already exists (same ID), it replaces the old entry.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(item: Product)

    // Deletes a specific product from the wishlist using its ID.
    @Query("DELETE FROM wishlist WHERE id =  :productId")
    suspend fun deleteWishlistItem(productId: Int)

    // Removes all items from the wishlist table (clears the entire wishlist).
    @Query("DELETE FROM wishlist")
    suspend fun clearWishlist()

    // Checks whether a specific product is already in the wishlist.
    // Returns true if it exists, otherwise false.
    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE id = :productId)")
    suspend fun isInWishlist(productId: Int): Boolean

    // Counts and returns the total number of items currently in the wishlist.
    @Query("SELECT COUNT(*) FROM wishlist")
    suspend fun getWishlistCount(): Int
}