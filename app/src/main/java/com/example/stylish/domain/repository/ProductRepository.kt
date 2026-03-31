package com.example.stylish.domain.repository
import com.example.stylish.domain.models.CategoryItem
import com.example.stylish.data.dto.Product
import com.example.stylish.domain.util.Result

interface ProductRepository{
    suspend fun getProducts(): Result<List<Product>>
    suspend fun searchProducts(query: String): Result<List<Product>>
    suspend fun getCategories(): Result<List<CategoryItem>>
    suspend fun getProductsByCategory(category: String): Result<List<Product>>
}