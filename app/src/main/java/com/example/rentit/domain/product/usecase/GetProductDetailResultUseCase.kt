package com.example.rentit.domain.product.usecase

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.data.product.mapper.toProductDetailModel
import com.example.rentit.domain.product.model.ProductDetailResultModel
import com.example.rentit.domain.product.repository.ProductRepository
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

/**
 * 상품 상세 정보를 조회하고, 부가 정보를 함께 가공
 *
 * - 카테고리 ID를 카테고리 이름으로 매핑
 * - 현재 로그인한 사용자가 상품 소유자인지 여부 확인
 * - 소유자인 경우, 대기 중(PENDING) 예약 요청 수 계산
 *
 * 결과로 [ProductDetailResultModel] 반환
 */

class GetProductDetailResultUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val getCategoryMapUseCase: GetCategoryMapUseCase
) {
    suspend operator fun invoke(productId: Int): Result<ProductDetailResultModel> {
        return runCatching {
            val productDetail = productRepository.getProductDetail(productId).getOrThrow().product
            val categoryMap = getCategoryMapUseCase().getOrThrow()

            val productDetailModel = productDetail.toProductDetailModel(categoryMap)

            val authUserId = userRepository.getAuthUserIdFromPrefs()
            val isUserOwner = productDetail.owner.userId == authUserId

            val requestCount = if (isUserOwner) {
                productRepository.getProductRequestList(productId)
                    .getOrThrow().reservations.filter { it.status == RentalStatus.PENDING }.size
            } else null

            ProductDetailResultModel(productDetailModel, requestCount)
        }
    }
}