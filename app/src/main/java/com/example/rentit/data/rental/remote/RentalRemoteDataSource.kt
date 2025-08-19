package com.example.rentit.data.rental.remote

import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import retrofit2.Response
import javax.inject.Inject

class RentalRemoteDataSource @Inject constructor(
    private val rentalApiService: RentalApiService
) {
    suspend fun getRentalDetail(productId: Int, reservationId: Int): Response<RentalDetailResponseDto> {
        return rentalApiService.getRentalDetail(productId, reservationId)
    }
}