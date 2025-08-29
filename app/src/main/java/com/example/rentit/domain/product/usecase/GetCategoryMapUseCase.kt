package com.example.rentit.domain.product.usecase

import com.example.rentit.domain.product.model.Category
import com.example.rentit.domain.product.repository.ProductRepository
import javax.inject.Inject

class GetCategoryMapUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(): Result<Map<Int, Category>> {
        return runCatching {
            productRepository.getCategories().getOrThrow().categories
                .associate { it.id to Category(it.name, it.isParent, it.parentId) }
        }
    }
}