package com.example.rentit.data.rental.remote

import com.example.rentit.common.enums.PhotoRegistrationType
import com.example.rentit.data.rental.dto.PhotoRegistrationResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoUploadApiService {
    @Multipart
    @POST("api/v1/products/{productId}/reservations/{reservationId}/photos")
    suspend fun postPhotoRegistration(
        @Path("productId") productId: Int,
        @Path("reservationId") reservationId: Int,
        @Query("type") type: PhotoRegistrationType,
        @Part images: List<MultipartBody.Part>
    ): Response<PhotoRegistrationResponseDto>
}