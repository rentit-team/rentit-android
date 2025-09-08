package com.example.rentit.domain.product.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.product.mapper.toChatRoomSummaryModel
import com.example.rentit.domain.product.model.ProductChatRoomSummaryModel
import com.example.rentit.domain.product.repository.ProductRepository
import javax.inject.Inject

/**
 * 특정 상품에 대한 채팅방 요약 정보를 가져오는 UseCase
 *
 * - 상품 ID를 기반으로 상품 상세 정보를 가져옴
 * - 상품 정보를 채팅방 요약 도메인 모델(ProductChatRoomSummaryModel)로 변환
 */

class GetChatRoomProductSummaryUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(productId: Int): Result<ProductChatRoomSummaryModel> {
        return productRepository.getProductDetail(productId)
            .map { it.product.toChatRoomSummaryModel() }
    }
}