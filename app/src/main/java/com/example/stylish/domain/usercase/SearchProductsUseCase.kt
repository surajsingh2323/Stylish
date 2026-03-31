package com.example.stylish.domain.usercase

import com.example.stylish.data.dto.Product
import com.example.stylish.domain.repository.ProductRepository
import javax.inject.Inject
import com.example.stylish.domain.util.Result

class SearchProductsUseCase @Inject constructor(
    private val repository: ProductRepository
){
    suspend operator fun invoke(query: String): Result<List<Product>>{
        return if (query.isBlank()){
            // If query is empty, return all products
            repository.getProducts()
        } else{
            // Otherwise, search with the query
            repository.searchProducts(query)
        }
    }
}