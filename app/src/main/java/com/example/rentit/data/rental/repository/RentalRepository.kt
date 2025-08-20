package com.example.rentit.data.rental.repository

import com.example.rentit.common.exception.ServerException
import com.example.rentit.common.exception.rental.EmptyBodyException
import com.example.rentit.data.rental.dto.CourierNamesResponseDto
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.TrackingRegistrationRequestDto
import com.example.rentit.data.rental.dto.TrackingRegistrationResponseDto
import com.example.rentit.data.rental.remote.RentalRemoteDataSource
import javax.inject.Inject

class RentalRepository @Inject constructor(
    private val rentalRemoteDataSource: RentalRemoteDataSource
){
    suspend fun getRentalDetail(productId: Int, reservationId: Int): Result<RentalDetailResponseDto> {
        return runCatching {
            val response = rentalRemoteDataSource.getRentalDetail(productId, reservationId)
            if (response.isSuccessful) {
                response.body() ?: throw EmptyBodyException()
            } else {
                throw ServerException()
            }
        }
    }

    suspend fun postTrackingRegistration(productId: Int, reservationId: Int, body: TrackingRegistrationRequestDto): Result<TrackingRegistrationResponseDto?> {
        return runCatching {
            val response = rentalRemoteDataSource.postTrackingRegistration(productId, reservationId, body)
            if (response.isSuccessful) {
                response.body()
            } else {
                throw ServerException()
            }
        }
    }

    suspend fun getCourierNames(): Result<CourierNamesResponseDto> {
        return runCatching {
            val response = rentalRemoteDataSource.getCourierNames()
            if (response.isSuccessful) {
                response.body() ?: throw EmptyBodyException()
            } else {
                throw ServerException()
            }
        }
    }
}