package com.example.rentit.domain.product.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.product.mapper.toChatRoomSummaryModel
import com.example.rentit.domain.product.model.ProductChatRoomSummaryModel
import com.example.rentit.domain.product.repository.ProductRepository
import javax.inject.Inject

class GetChatRoomProductSummaryUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(productId: Int): Result<ProductChatRoomSummaryModel> {
        return productRepository.getProductDetail(productId)
            .map { it.product.toChatRoomSummaryModel() }
    }
}