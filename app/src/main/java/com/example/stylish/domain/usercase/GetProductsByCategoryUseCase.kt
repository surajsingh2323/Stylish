package com.example.stylish.domain.usercase

import com.example.stylish.data.dto.Product
import com.example.stylish.domain.repository.ProductRepository
import javax.inject.Inject
import com.example.stylish.domain.util.Result

class GetProductsByCategoryUseCase @Inject constructor(
    private val repository: ProductRepository
){
    suspend operator fun invoke(category: String): Result<List<Product>>{
        return repository.getProductsByCategory(category)
    }
}