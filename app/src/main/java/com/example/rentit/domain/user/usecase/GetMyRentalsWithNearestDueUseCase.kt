package com.example.rentit.domain.user.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.user.mapper.toModel
import com.example.rentit.data.user.mapper.toMyRentalItemModel
import com.example.rentit.domain.user.model.MyRentalsWithNearestDueModel
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

class GetMyRentalsWithNearestDueUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(): Result<MyRentalsWithNearestDueModel> {
        return runCatching {
            val response = userRepository.getMyRentalList().getOrThrow()

            val myRentalList = response.myReservations.map { it.toMyRentalItemModel() }
            val nearestDueItem = response.nearestDueItem?.toModel()

            MyRentalsWithNearestDueModel(myRentalList, nearestDueItem)
        }
    }
}