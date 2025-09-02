package com.example.rentit.domain.product.usecase

import com.example.rentit.data.product.mapper.toProductDetailModel
import com.example.rentit.domain.product.model.ProductDetailModel
import com.example.rentit.domain.product.repository.ProductRepository
import javax.inject.Inject

/**
 * 상품 상세 정보를 가져오고, 카테고리 이름 매핑까지 포함
 */

class GetProductDetailUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val getCategoryMapUseCase: GetCategoryMapUseCase
) {
    suspend operator fun invoke(productId: Int): Result<ProductDetailModel> {
        return runCatching {
            val productDetail = productRepository.getProductDetail(productId).getOrThrow().product
            val categoryMap = getCategoryMapUseCase().getOrThrow()

            productDetail.toProductDetailModel(categoryMap)
        }
    }
}