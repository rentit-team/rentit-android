package com.example.rentit.data.rental.remote

import com.example.rentit.data.rental.dto.CourierNamesResponseDto
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.TrackingRegistrationRequestDto
import com.example.rentit.data.rental.dto.TrackingRegistrationResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface RentalApiService {
    @GET("api/v1/products/{productId}/reservations/{reservationId}")
    @Headers("Content-Type: application/json")
    suspend fun getRentalDetail(
        @Path("productId") productId: Int,
        @Path("reservationId") reservationId: Int,
    ): Response<RentalDetailResponseDto>

    @POST("api/v1/products/{productId}/reservations/{reservationId}/tracking")
    @Headers("Content-Type: application/json")
    suspend fun postTrackingRegistration(
        @Path("productId") productId: Int,
        @Path("reservationId") reservationId: Int,
        @Body trackingRegistrationRequestDto: TrackingRegistrationRequestDto
    ): Response<TrackingRegistrationResponseDto>

    @GET("api/v1/delivery/couriers")
    @Headers("Content-Type: application/json")
    suspend fun getCourierNames(): Response<CourierNamesResponseDto>
}