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
import javax.inject.Inject

class RentalRemoteDataSource @Inject constructor(
    private val rentalApiService: RentalApiService,
    private val photoApiService: PhotoUploadApiService,
) {
    suspend fun getRentalDetail(productId: Int, reservationId: Int): Response<RentalDetailResponseDto> {
        return rentalApiService.getRentalDetail(productId, reservationId)
    }

    suspend fun postTrackingRegistration(productId: Int, reservationId: Int, body: TrackingRegistrationRequestDto): Response<TrackingRegistrationResponseDto> {
        return rentalApiService.postTrackingRegistration(productId, reservationId, body)
    }

    suspend fun getCourierNames(): Response<CourierNamesResponseDto> {
        return rentalApiService.getCourierNames()
    }
    suspend fun updateRentalStatus(productId: Int, reservationId: Int, request: UpdateRentalStatusRequestDto): Response<Unit> {
        return rentalApiService.updateRentalStatus(productId, reservationId, request)
    }
    suspend fun postPhotoRegistration(productId: Int, reservationId: Int, type: PhotoRegistrationType, images: List<MultipartBody.Part>): Response<PhotoRegistrationResponseDto> {
        return photoApiService.postPhotoRegistration(productId, reservationId, type, images)
    }
}