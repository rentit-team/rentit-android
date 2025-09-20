package com.example.rentit.data.user.remote

import com.example.rentit.data.user.dto.MyInfoResponseDto
import com.example.rentit.data.user.dto.MyProductListResponseDto
import com.example.rentit.data.user.dto.MyProductsRentalListResponseDto
import com.example.rentit.data.user.dto.MyRentalListResponseDto
import retrofit2.Response
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApiService: UserApiService,
) {
    suspend fun getMyInfo(): Response<MyInfoResponseDto> {
        return userApiService.getMyInfo()
    }

    suspend fun getMyProductList(): Response<MyProductListResponseDto> {
        return userApiService.getMyProductList()
    }

    suspend fun getMyRentalList(): Response<MyRentalListResponseDto> {
        return userApiService.getMyRentalList()
    }

    suspend fun getMyProductsRentalList(): Response<MyProductsRentalListResponseDto> {
        return userApiService.getMyProductsRentalList()
    }
}