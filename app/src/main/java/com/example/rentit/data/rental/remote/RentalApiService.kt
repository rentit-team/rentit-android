package com.example.rentit.data.rental.remote

import com.example.rentit.common.enums.RentalProcessType
import com.example.rentit.data.rental.dto.RentalHistoriesResponseDto
import com.example.rentit.data.rental.dto.CourierNamesResponseDto
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.RentalPhotoResponseDto
import com.example.rentit.data.rental.dto.TrackingRegistrationRequestDto
import com.example.rentit.data.rental.dto.TrackingRegistrationResponseDto
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface RentalApiService {

    @GET("api/v1/products/{productId}/reservations")
    @Headers("Content-Type: application/json")
    suspend fun getRentalHistoriesByProduct(@Path("productId") productId: Int): Response<RentalHistoriesResponseDto>

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

    @GET("api/v1/products/{productId}/reservations/{reservationId}/photos")
    @Headers("Content-Type: application/json")
    suspend fun getRentalPhotos(
        @Path("productId") productId: Int,
        @Path("reservationId") reservationId: Int,
    ): Response<RentalPhotoResponseDto>

    @GET("api/v1/products/{productId}/reservations/{reservationId}/receipt")
    @Streaming
    suspend fun getRentalReceipt(
        @Path("productId") productId: Int,
        @Path("reservationId") reservationId: Int,
        @Query("type") type: RentalProcessType
    ): Response<ResponseBody>
}
