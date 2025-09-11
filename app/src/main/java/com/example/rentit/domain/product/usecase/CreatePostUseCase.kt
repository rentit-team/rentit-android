package com.example.rentit.domain.product.usecase

import com.example.rentit.data.product.dto.CreatePostRequestDto
import com.example.rentit.data.product.dto.CreatePostResponseDto
import com.example.rentit.data.product.dto.PeriodDto
import com.example.rentit.domain.product.repository.ProductRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * 게시글 생성 UseCase
 *
 * - 입력값을 기반으로 RequestBody 생성하고 thumbnail part와 함께 Repository에 전달
 * - 성공 시 생성된 게시글 응답을 반환, 실패 시 예외를 Result로 래핑
 */

class CreatePostUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) {

    suspend operator fun invoke(
        thumbnailPart: MultipartBody.Part?,
        title: String,
        content: String,
        selectedCategoryIdList: List<Int>,
        periodSliderPosition: ClosedFloatingPointRange<Float>,
        price: Int,
    ): Result<CreatePostResponseDto> {
        return runCatching {
            val period = PeriodDto("daily", periodSliderPosition.start.toInt(), periodSliderPosition.endInclusive.toInt())
            val postData = CreatePostRequestDto(title, content, selectedCategoryIdList, period, price.toDouble(), null)

            val payloadJson = Gson().toJson(postData)
            val requestBody = payloadJson.toRequestBody("application/json".toMediaTypeOrNull())

            productRepository.createPost(requestBody, thumbnailPart).getOrThrow()
        }
    }
}