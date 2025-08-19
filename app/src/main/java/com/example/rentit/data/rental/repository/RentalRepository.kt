package com.example.rentit.data.rental.repository

import com.example.rentit.common.exception.ServerException
import com.example.rentit.common.exception.rental.EmptyBodyException
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
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
}