package com.example.stylish.domain.usercase

import com.example.stylish.data.dto.Product
import com.example.stylish.domain.repository.ProductRepository
import com.example.stylish.domain.util.Result
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
){
    suspend operator fun invoke(): Result<List<Product>>{
        return repository.getProducts()
    }
}