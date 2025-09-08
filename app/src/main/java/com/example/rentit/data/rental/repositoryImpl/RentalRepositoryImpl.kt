package com.example.rentit.data.rental.repositoryImpl

import com.example.rentit.common.enums.PhotoRegistrationType
import com.example.rentit.core.network.getOrThrow
import com.example.rentit.data.rental.dto.CourierNamesResponseDto
import com.example.rentit.data.rental.dto.PhotoRegistrationResponseDto
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.RentalPhotoResponseDto
import com.example.rentit.data.rental.dto.TrackingRegistrationRequestDto
import com.example.rentit.data.rental.dto.TrackingRegistrationResponseDto
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import com.example.rentit.data.rental.remote.RentalRemoteDataSource
import com.example.rentit.domain.rental.repository.RentalRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class RentalRepositoryImpl @Inject constructor(
    private val rentalRemoteDataSource: RentalRemoteDataSource
): RentalRepository {
    override suspend fun getRentalDetail(productId: Int, reservationId: Int): Result<RentalDetailResponseDto> {
        val response = rentalRemoteDataSource.getRentalDetail(productId, reservationId)
        return response.getOrThrow()
    }

    override suspend fun postTrackingRegistration(productId: Int, reservationId: Int, body: TrackingRegistrationRequestDto): Result<TrackingRegistrationResponseDto?> {
        val response = rentalRemoteDataSource.postTrackingRegistration(productId, reservationId, body)
        return response.getOrThrow()
    }

    override suspend fun getCourierNames(): Result<CourierNamesResponseDto> {
        val response = rentalRemoteDataSource.getCourierNames()
        return response.getOrThrow()
    }

    override suspend fun updateRentalStatus(productId: Int, reservationId: Int, request: UpdateRentalStatusRequestDto): Result<Unit> {
        val response = rentalRemoteDataSource.updateRentalStatus(productId, reservationId, request)
        return response.getOrThrow()
    }

    override suspend fun postPhotoRegistration(productId: Int, reservationId: Int, type: PhotoRegistrationType, images: List<MultipartBody.Part>): Result<PhotoRegistrationResponseDto> {
        val response = rentalRemoteDataSource.postPhotoRegistration(productId, reservationId, type, images)
        return response.getOrThrow()
    }

    override suspend fun getRentalPhotos(productId: Int, reservationId: Int): Result<RentalPhotoResponseDto> {
        val response = rentalRemoteDataSource.getRentalPhotos(productId, reservationId)
        return response.getOrThrow()
    }
}