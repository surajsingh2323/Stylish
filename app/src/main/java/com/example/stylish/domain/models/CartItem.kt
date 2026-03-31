package com.example.stylish.domain.models
import kotlinx.serialization.Serializable
import com.example.stylish.data.dto.Product

@Serializable
data class CartItem(
    val product: Product,
    val quantity: Int = 1
)

