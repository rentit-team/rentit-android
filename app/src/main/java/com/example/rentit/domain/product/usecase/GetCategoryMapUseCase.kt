package com.example.rentit.domain.product.usecase

import com.example.rentit.domain.product.model.Category
import com.example.rentit.domain.product.repository.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 전체 카테고리를 가져와 ID를 키로 하는 Map으로 변환하고,
 * 내부 캐시에 저장한 뒤 반환
 */

@Singleton
class GetCategoryMapUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) {

    private var cachedCategoryMap: Map<Int, Category>? = null

    suspend operator fun invoke(): Result<Map<Int, Category>> {
        cachedCategoryMap?.let { return Result.success(it) }

        return runCatching {
            val categoryMap = productRepository.getCategories().getOrThrow().categories
                .associate { it.id to Category(it.name, it.isParent, it.parentId) }
            cachedCategoryMap = categoryMap
            categoryMap
        }
    }
}