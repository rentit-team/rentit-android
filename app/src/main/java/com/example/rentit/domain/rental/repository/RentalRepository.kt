package com.example.rentit.domain.rental.repository

import com.example.rentit.common.enums.PhotoRegistrationType
import com.example.rentit.data.rental.dto.CourierNamesResponseDto
import com.example.rentit.data.rental.dto.PhotoRegistrationResponseDto
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.RentalPhotoResponseDto
import com.example.rentit.data.rental.dto.TrackingRegistrationRequestDto
import com.example.rentit.data.rental.dto.TrackingRegistrationResponseDto
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import okhttp3.MultipartBody

interface RentalRepository {
    suspend fun getRentalDetail(productId: Int, reservationId: Int): Result<RentalDetailResponseDto>

    suspend fun postTrackingRegistration(productId: Int, reservationId: Int, body: TrackingRegistrationRequestDto): Result<TrackingRegistrationResponseDto?>

    suspend fun getCourierNames(): Result<CourierNamesResponseDto>

    suspend fun updateRentalStatus(productId: Int, reservationId: Int, request: UpdateRentalStatusRequestDto): Result<Unit>

    suspend fun postPhotoRegistration(productId: Int, reservationId: Int, type: PhotoRegistrationType, images: List<MultipartBody.Part>): Result<PhotoRegistrationResponseDto>

    suspend fun getRentalPhotos(productId: Int, reservationId: Int): Result<RentalPhotoResponseDto>
}