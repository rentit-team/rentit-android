package com.example.rentit.data.rental.repositoryImpl

import com.example.rentit.common.enums.PhotoRegistrationType
import com.example.rentit.core.network.safeApiCall
import com.example.rentit.data.rental.dto.RentalHistoriesResponseDto
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
    override suspend fun getRentalHistoriesByProduct(productId: Int): Result<RentalHistoriesResponseDto> {
        return safeApiCall { rentalRemoteDataSource.getProductRequestList(productId) }
    }

    override suspend fun getRentalDetail(productId: Int, reservationId: Int): Result<RentalDetailResponseDto> {
        return safeApiCall { rentalRemoteDataSource.getRentalDetail(productId, reservationId) }
    }

    override suspend fun postTrackingRegistration(productId: Int, reservationId: Int, body: TrackingRegistrationRequestDto): Result<TrackingRegistrationResponseDto?> {
        return safeApiCall { rentalRemoteDataSource.postTrackingRegistration(productId, reservationId, body) }
    }

    override suspend fun getCourierNames(): Result<CourierNamesResponseDto> {
        return safeApiCall(rentalRemoteDataSource::getCourierNames)
    }

    override suspend fun updateRentalStatus(productId: Int, reservationId: Int, request: UpdateRentalStatusRequestDto): Result<Unit> {
        return safeApiCall { rentalRemoteDataSource.updateRentalStatus(productId, reservationId, request) }
    }

    override suspend fun postPhotoRegistration(productId: Int, reservationId: Int, type: PhotoRegistrationType, images: List<MultipartBody.Part>): Result<PhotoRegistrationResponseDto> {
        return safeApiCall { rentalRemoteDataSource.postPhotoRegistration(productId, reservationId, type, images) }
    }

    override suspend fun getRentalPhotos(productId: Int, reservationId: Int): Result<RentalPhotoResponseDto> {
        return safeApiCall { rentalRemoteDataSource.getRentalPhotos(productId, reservationId) }
    }
}