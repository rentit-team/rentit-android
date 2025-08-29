package com.example.rentit.domain.product.usecase

import com.example.rentit.domain.product.model.Category
import com.example.rentit.domain.product.model.ProductWithCategory
import com.example.rentit.domain.product.repository.ProductRepository
import javax.inject.Inject

class GetProductListWithCategoryUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(categoryMap: Map<Int, Category>): Result<List<ProductWithCategory>> {
        return runCatching {
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
                    parentCategoryIds = it.categories.mapNotNull { categoryId -> categoryMap[categoryId]?.parentId }.distinct(),
                    categoryNames = it.categories.mapNotNull { categoryId -> categoryMap[categoryId]?.name },
                    createdAt = it.createdAt
                )
            }
        }
    }
}