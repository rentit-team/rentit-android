package com.example.rentit.data.product.repositoryImpl

import com.example.rentit.common.exception.ExpiredTokenException
import com.example.rentit.common.exception.MissingTokenException
import com.example.rentit.common.exception.ServerException
import com.example.rentit.domain.rental.exception.EmptyBodyException
import com.example.rentit.data.product.dto.ResvRequestDto
import com.example.rentit.data.product.dto.ResvResponseDto
import com.example.rentit.data.product.dto.CategoryListResponseDto
import com.example.rentit.data.product.dto.CreatePostResponseDto
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.ProductReservedDatesResponseDto
import com.example.rentit.data.product.dto.ProductListResponseDto
import com.example.rentit.data.product.dto.RequestHistoryResponseDto
import com.example.rentit.data.product.remote.ProductRemoteDataSource
import com.example.rentit.domain.product.exception.AccessNotAllowedException
import com.example.rentit.domain.product.exception.ResvAlreadyExistException
import com.example.rentit.domain.product.exception.ResvByOwnerException
import com.example.rentit.domain.product.repository.ProductRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource
): ProductRepository {
    override suspend fun getProductList(): Result<ProductListResponseDto> {
        return runCatching {
            val response = productRemoteDataSource.getProductList()
            if(response.isSuccessful) {
                response.body() ?: throw EmptyBodyException()
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Client Error"
                throw when(response.code()) {
                    401 -> ExpiredTokenException(errorMsg)
                    500 -> ServerException(errorMsg)
                    else -> Exception(errorMsg)
                }
            }
        }
    }

    override suspend fun getProductDetail(productId: Int): Result<ProductDetailResponseDto> {
        return runCatching {
            val response = productRemoteDataSource.getProductDetail(productId)
            if(response.isSuccessful) {
                response.body() ?: throw EmptyBodyException()
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Client Error"
                throw if(response.code() == 401) MissingTokenException(errorMsg) else Exception(errorMsg)
            }
        }
    }

    override suspend fun getReservedDates(productId: Int): Result<ProductReservedDatesResponseDto> {
        return runCatching {
            val response = productRemoteDataSource.getReservedDates(productId)
            if(response.isSuccessful) {
                response.body() ?: throw EmptyBodyException()
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Client Error"
                when(response.code()) {
                    401 -> throw MissingTokenException(errorMsg)
                    500 -> throw ServerException(errorMsg)
                    else -> throw Exception(errorMsg)
                }
            }
        }
    }

    override suspend fun postResv(productId: Int, request: ResvRequestDto): Result<ResvResponseDto> {
        return runCatching {
            val response = productRemoteDataSource.postResv(productId, request)
            if(response.isSuccessful) {
                response.body() ?: throw EmptyBodyException()
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Client Error"
                throw when(response.code()) {
                    403 -> ResvByOwnerException(errorMsg)   // 판매자가 요청한 경우
                    409 -> ResvAlreadyExistException(errorMsg)  // 이미 동일 기간의 예약이 1건 이상 존재하는 경우
                    500 -> ServerException(errorMsg)
                    else -> Exception(errorMsg)
                }
            }
        }
    }

    override suspend fun getCategories(): Result<CategoryListResponseDto> {
        return runCatching {
            val response = productRemoteDataSource.getCategories()
            if(response.isSuccessful) {
                response.body() ?: throw EmptyBodyException()
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Client Error"
                throw Exception(errorMsg)
            }
        }
    }

    override suspend fun createPost(payLoad: RequestBody, thumbnailImg: MultipartBody.Part?): Result<CreatePostResponseDto> {
        return try {
            val response = productRemoteDataSource.createPost(payLoad, thumbnailImg)
            when(response.code()) {
                200 -> {
                    val body = response.body()
                    if(body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                }
                500 -> {
                    Result.failure(Exception("Server error"))
                }
                else -> {
                    Result.failure(Exception("Unexpected error"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductRequestList(productId: Int): Result<RequestHistoryResponseDto> {
        return runCatching {
            val response = productRemoteDataSource.getProductRequestList(productId)
            if(response.isSuccessful){
                response.body() ?: throw EmptyBodyException()
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Client Error"
                throw when(response.code()) {
                    401 -> MissingTokenException(errorMsg)
                    403 -> AccessNotAllowedException(errorMsg)
                    500 -> ServerException(errorMsg)
                    else -> Exception(errorMsg)
                }
            }
        }
    }
}
