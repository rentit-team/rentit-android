package com.example.rentit.data.product.usecase

import com.example.rentit.data.product.model.ProductWithCategory
import com.example.rentit.data.product.repository.ProductRepository
import javax.inject.Inject

class GetProductListWithCategoryUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<ProductWithCategory>> {
        return runCatching {
            val categoryMap = productRepository.getCategories().getOrThrow().categories
                .filter { !it.isParent }
                .associate { it.id to it.name }

            val productList = productRepository.getProductList().getOrThrow().products

            productList.reversed().map {
                ProductWithCategory(
                    productId = it.productId,
                    title = it.title,
                    thumbnailImgUrl = it.thumbnailImgUrl,
                    price = it.price,
                    minPeriod = it.period?.min,
                    maxPeriod = it.period?.max,
                    status = it.status,
                    categoryNames = it.categories.mapNotNull { categoryId -> categoryMap[categoryId] },
                    createdAt = it.createdAt
                )
            }
        }
    }
}