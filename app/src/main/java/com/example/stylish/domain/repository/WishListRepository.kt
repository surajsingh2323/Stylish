package com.example.stylish.domain.repository

import com.example.stylish.data.dto.Product
import kotlinx.coroutines.flow.Flow

interface WishListRepository {
    fun getWishlistProducts(): Flow<List<Product>>
    suspend fun addToWishlist(product: Product)
    suspend fun removeFromWishlist(productId:Int)
    suspend fun isInWishlist(productId: Int): Boolean
    suspend fun clearWishlist()
}