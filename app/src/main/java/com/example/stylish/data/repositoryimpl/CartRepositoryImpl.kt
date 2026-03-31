package com.example.stylish.data.repositoryimpl

import com.example.stylish.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import com.example.stylish.data.dto.Product
import com.example.stylish.domain.models.CartItem
import com.example.stylish.data.local.CartDataStore

class CartRepositoryImpl(
    private val cartDataStore: CartDataStore
) : CartRepository {
    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDataStore.cartItems
    }

    override suspend fun addToCart(product: Product, quantity: Int) {
        cartDataStore.addToCart(product,quantity)
    }

    override suspend fun removeFromCart(productId: Int) {
        cartDataStore.removeFromCart(productId)
    }

    override suspend fun updateQuantity(productId: Int, quantity: Int) {
        cartDataStore.updateQuantity(productId,quantity)
    }

    override suspend fun clearCart() {
        cartDataStore.clearCart()
    }

    override suspend fun getCartItemCount(): Int {
        return cartDataStore.getCartItemCount()
    }
}