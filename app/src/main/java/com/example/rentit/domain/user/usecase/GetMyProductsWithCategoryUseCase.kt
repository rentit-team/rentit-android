package com.example.rentit.domain.user.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.user.mapper.toMyProductItemModel
import com.example.rentit.domain.product.usecase.GetCategoryMapUseCase
import com.example.rentit.domain.user.model.MyProductItemModel
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

class GetMyProductsWithCategoryUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val getCategoryMapUseCase: GetCategoryMapUseCase
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(): Result<List<MyProductItemModel>> {
        return runCatching {
            val response = userRepository.getMyProductList().getOrThrow()
            val categoryMap = getCategoryMapUseCase().getOrThrow()

            response.myProducts.map { it.toMyProductItemModel(categoryMap) }
        }
    }
}