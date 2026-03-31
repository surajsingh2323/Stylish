package com.example.stylish.data.repositoryimpl

import com.example.stylish.data.dto.Product
import com.example.stylish.data.local.dao.WishlistDao
import com.example.stylish.domain.repository.WishListRepository
import kotlinx.coroutines.flow.Flow

class WishlistRepositoryImpl(
    private val wishlistDao: WishlistDao
) : WishListRepository {
    override fun getWishlistProducts(): Flow<List<Product>> {
        return wishlistDao.getAllWishlistItems()
    }

    override suspend fun addToWishlist(product: Product) {
        wishlistDao.insertWishlistItem(product)
    }

    override suspend fun removeFromWishlist(productId: Int) {
        wishlistDao.deleteWishlistItem(productId)
    }

    override suspend fun isInWishlist(productId: Int): Boolean {
        return wishlistDao.isInWishlist(productId)
    }

    override suspend fun clearWishlist() {
        wishlistDao.clearWishlist()
    }
}