package com.example.rentit.domain.user.usecase

import com.example.rentit.data.user.mapper.toMyProductItemModel
import com.example.rentit.domain.product.usecase.GetCategoryMapUseCase
import com.example.rentit.domain.user.model.MyProductItemModel
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

/**
 * 사용자가 등록한 상품 리스트를 카테고리 정보와 함께 가져오는 UseCase
 *
 * - 서버에서 사용자의 상품 리스트 조회
 * - 각 상품에 대해 카테고리 정보를 매핑하여 도메인 모델(MyProductItemModel)로 변환
 */

class GetMyProductsWithCategoryUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val getCategoryMapUseCase: GetCategoryMapUseCase
) {
    suspend operator fun invoke(): Result<List<MyProductItemModel>> {
        return runCatching {
            val response = userRepository.getMyProductList().getOrThrow()
            val categoryMap = getCategoryMapUseCase().getOrThrow()

            response.myProducts.map { it.toMyProductItemModel(categoryMap) }
        }
    }
}