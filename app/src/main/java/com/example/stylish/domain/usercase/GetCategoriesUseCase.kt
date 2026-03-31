package com.example.stylish.domain.usercase

import com.example.stylish.domain.repository.ProductRepository
import javax.inject.Inject
import com.example.stylish.domain.models.CategoryItem
import com.example.stylish.domain.util.Result

class GetCategoriesUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<CategoryItem>>{
        return repository.getCategories()
    }
}