package com.example.rentit.domain.user.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.data.user.mapper.toModel
import com.example.rentit.data.user.mapper.toMyRentalItemModel
import com.example.rentit.domain.user.model.MyRentalsWithNearestDueModel
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

/**
 * 사용자의 대여 리스트를 가져오고, 가장 가까운 반납 예정 아이템과 유효한 대여 수를 계산하는 UseCase
 *
 * - 서버에서 사용자의 모든 예약 정보를 조회
 * - 각 예약 정보를 도메인 모델(MyRentalItemModel)로 변환
 * - 가장 가까운 반납 예정 아이템(nearestDueItem)을 도메인 모델로 변환
 * - 취소, 거절, 대기 상태가 아닌 유효한 대여 수를 계산
 */

class GetMyRentalsWithNearestDueUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(): Result<MyRentalsWithNearestDueModel> {
        return runCatching {
            val response = userRepository.getMyRentalList().getOrThrow()

            val myRentalList = response.myReservations.map { it.toMyRentalItemModel() }
            val nearestDueItem = response.nearestDueItem?.toModel()
            val myValidRentalCount = myRentalList.count {
                it.status !in listOf(
                    RentalStatus.CANCELED,
                    RentalStatus.REJECTED,
                    RentalStatus.PENDING
                )
            }

            MyRentalsWithNearestDueModel(myRentalList, myValidRentalCount, nearestDueItem)
        }
    }
}