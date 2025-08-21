package com.example.rentit.data.rental.remote

import com.example.rentit.common.enums.PhotoRegistrationType
import com.example.rentit.data.rental.dto.CourierNamesResponseDto
import com.example.rentit.data.rental.dto.PhotoRegistrationResponseDto
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.TrackingRegistrationRequestDto
import com.example.rentit.data.rental.dto.TrackingRegistrationResponseDto
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @PATCH("api/v1/products/{productId}/reservations/{reservationId}")
    @Headers("Content-Type: application/json")
    suspend fun updateRentalStatus(
        @Path("productId") productId: Int,
        @Path("reservationId") reservationId: Int,
        @Body request: UpdateRentalStatusRequestDto
    ): Response<Unit>

    @Multipart
    @POST("api/v1/products/{productId}/reservations/{reservationId}/photos")
    suspend fun postPhotoRegistration(
        @Path("productId") productId: Int,
        @Path("reservationId") reservationId: Int,
        @Query("type") type: PhotoRegistrationType,
        @Part images: List<MultipartBody.Part>
    ): Response<PhotoRegistrationResponseDto>
}