package com.example.rentit.domain.product.usecase

import com.example.rentit.domain.product.model.CategoryModel
import com.example.rentit.domain.product.model.ProductWithCategoryModel
import com.example.rentit.domain.product.repository.ProductRepository
import javax.inject.Inject

/**
 * 상품 리스트를 카테고리 정보와 함께 가져오는 UseCase
 *
 * - 서버에서 상품 리스트를 조회
 * - 각 상품에 대해 카테고리 정보를 매핑하여 도메인 모델(ProductWithCategoryModel)로 변환
 */

class GetProductListWithCategoryUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(categoryMap: Map<Int, CategoryModel>): Result<List<ProductWithCategoryModel>> {
        return runCatching {
            val productList = productRepository.getProductList().getOrThrow().products

            productList.reversed().map {
                ProductWithCategoryModel(
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