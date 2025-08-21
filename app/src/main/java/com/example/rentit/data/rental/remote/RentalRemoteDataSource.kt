package com.example.rentit.data.rental.remote

import com.example.rentit.data.rental.dto.CourierNamesResponseDto
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.TrackingRegistrationRequestDto
import com.example.rentit.data.rental.dto.TrackingRegistrationResponseDto
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import retrofit2.Response
import javax.inject.Inject

class RentalRemoteDataSource @Inject constructor(
    private val rentalApiService: RentalApiService
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
}