package com.example.rentit.data.rental.repositoryImpl

import com.example.rentit.common.enums.PhotoRegistrationType
import com.example.rentit.common.exception.MissingTokenException
import com.example.rentit.common.exception.ServerException
import com.example.rentit.common.exception.rental.RentalNotFoundException
import com.example.rentit.common.exception.rental.EmptyBodyException
import com.example.rentit.data.rental.dto.CourierNamesResponseDto
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
        return runCatching {
            val response = rentalRemoteDataSource.getRentalDetail(productId, reservationId)
            if (response.isSuccessful) {
                response.body() ?: throw EmptyBodyException()
            } else {
                throw ServerException()
            }
        }
    }

    override suspend fun postTrackingRegistration(productId: Int, reservationId: Int, body: TrackingRegistrationRequestDto): Result<TrackingRegistrationResponseDto?> {
        return runCatching {
            val response = rentalRemoteDataSource.postTrackingRegistration(productId, reservationId, body)
            if (response.isSuccessful) {
                response.body()
            } else {
                throw ServerException()
            }
        }
    }

    override suspend fun getCourierNames(): Result<CourierNamesResponseDto> {
        return runCatching {
            val response = rentalRemoteDataSource.getCourierNames()
            if (response.isSuccessful) {
                response.body() ?: throw EmptyBodyException()
            } else {
                throw ServerException()
            }
        }
    }

    override suspend fun updateRentalStatus(productId: Int, reservationId: Int, request: UpdateRentalStatusRequestDto): Result<Unit> {
        return runCatching {
            val response = rentalRemoteDataSource.updateRentalStatus(productId, reservationId, request)
            if (!response.isSuccessful) {
                throw when (response.code()) {
                    401 -> MissingTokenException()
                    404 -> RentalNotFoundException()
                    else -> ServerException()
                }
            }
            Unit
        }
    }

    override suspend fun postPhotoRegistration(productId: Int, reservationId: Int, type: PhotoRegistrationType, images: List<MultipartBody.Part>): Result<Unit> {
        return runCatching {
            val response = rentalRemoteDataSource.postPhotoRegistration(productId, reservationId, type, images)
            if (!response.isSuccessful) {
                throw Exception()
            }
            Unit
        }
    }

    override suspend fun getRentalPhotos(productId: Int, reservationId: Int): Result<RentalPhotoResponseDto> {
        return runCatching {
            val response = rentalRemoteDataSource.getRentalPhotos(productId, reservationId)
            if (response.isSuccessful) {
                response.body() ?: throw EmptyBodyException()
            } else {
                throw ServerException()
            }
        }
    }
}