package com.example.rentit.data.rental.remote

import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RentalApiService {
    @GET("api/v1/products/{productId}/reservations/{reservationId}")
    @Headers("Content-Type: application/json")
    suspend fun getRentalDetail(
        @Path("productId") productId: Int,
        @Path("reservationId") reservationId: Int,
    ): Response<RentalDetailResponseDto>
}