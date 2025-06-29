package com.example.rentit.data.product.repository

import com.example.rentit.common.exception.ServerException
import com.example.rentit.common.exception.product.NotProductOwnerException
import com.example.rentit.common.exception.product.ReservationNotFoundException
import com.example.rentit.data.product.dto.BookingRequestDto
import com.example.rentit.data.product.dto.BookingResponseDto
import com.example.rentit.data.product.dto.CategoryListResponseDto
import com.example.rentit.data.product.dto.CreatePostResponseDto
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.ProductReservedDatesResponseDto
import com.example.rentit.data.product.dto.ProductListResponseDto
import com.example.rentit.data.product.dto.RequestHistoryResponseDto
import com.example.rentit.data.product.dto.UpdateBookingStatusRequestDto
import com.example.rentit.data.product.remote.ProductRemoteDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource
) {
    suspend fun getProductList(): Result<ProductListResponseDto> {
        return try {
            val response = productRemoteDataSource.getProductList()
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
    suspend fun getProductDetail(productId: Int): Result<ProductDetailResponseDto> {
        return try {
            val response = productRemoteDataSource.getProductDetail(productId)
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

    suspend fun getReservedDates(productId: Int): Result<ProductReservedDatesResponseDto> {
        return try {
            val response = productRemoteDataSource.getReservedDates(productId)
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

    suspend fun postBooking(productId: Int, request: BookingRequestDto): Result<BookingResponseDto> {
        return try {
            val response = productRemoteDataSource.postBooking(productId, request)
            when(response.code()) {
                200 -> {
                    val body = response.body()
                    if(body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                }
                // 판매자가 요청한 경우
                403 -> {
                    Result.failure(Exception("판매자가 예약 요청을 거절했어요"))
                }
                // 이미 동일 기간의 예약이 1건 이상 존재하는 경우
                409 -> {
                    Result.failure(Exception("선택하신 날짜는 이미 예약되었어요"))
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

    suspend fun getCategories(): Result<CategoryListResponseDto> {
        return try {
            val response = productRemoteDataSource.getCategories()
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

    suspend fun createPost(payLoad: RequestBody, thumbnailImg: MultipartBody.Part?): Result<CreatePostResponseDto> {
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

    suspend fun getProductRequestList(productId: Int): Result<RequestHistoryResponseDto> {
        return try {
            val response = productRemoteDataSource.getProductRequestList(productId)
            when(response.code()) {
                200 -> {
                    val body = response.body()
                    if(body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                }
                403 -> {
                    Result.failure(Exception("Renter is not allowed to access reservation list"))
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

    suspend fun updateBookingStatus(productId: Int, reservationId: Int, request: UpdateBookingStatusRequestDto): Result<Unit> {
        return try {
            val response = productRemoteDataSource.updateBookingStatus(productId, reservationId, request)
            when(response.code()) {
                200 -> Result.success(Unit)
                403 -> Result.failure(NotProductOwnerException())
                404 -> Result.failure(ReservationNotFoundException())
                500 -> Result.failure(ServerException())
                else -> Result.failure(Exception("Unexpected error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
