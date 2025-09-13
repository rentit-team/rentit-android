package com.example.rentit.domain.user.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.util.daysFromToday
import com.example.rentit.domain.user.model.MyProductsRentalModel
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

/**
 * 내 상품 대여 내역을 가져와서 상태(RentalStatus)별로 그룹화하고,
 * 각 상태별로 UI/화면에 필요한 형태로 가공하여 반환하는 UseCase
 */

class GetMyProductsRentalHistoryUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(): Result<Map<RentalStatus, List<MyProductsRentalModel>>> {
        return runCatching {
            val rentalHistories =
                userRepository.getMyProductsRentalList().getOrThrow().rentalHistory

            rentalHistories.groupBy { it.status }
                .mapValues { (status, histories) ->
                    when (status) {
                        RentalStatus.PENDING, RentalStatus.ACCEPTED -> {
                            histories.groupBy { it.productId }
                                .map { (productId, records) ->
                                    val first = records.first()
                                    MyProductsRentalModel(
                                        productId = productId,
                                        reservationId = first.reservationId,
                                        rentalCount = records.size,
                                        productThumbnailUrl = first.productThumbnailImgUrl,
                                        productTitle = first.productTitle,
                                        totalExpectRevenue = records.sumOf { it.totalPrice },
                                    )
                                }
                        }

                        RentalStatus.PAID, RentalStatus.RENTING -> {
                            histories.map {
                                MyProductsRentalModel(
                                    productId = it.productId,
                                    reservationId = it.reservationId,
                                    productThumbnailUrl = it.productThumbnailImgUrl,
                                    productTitle = it.productTitle,
                                    renterNickname = it.renterNickname,
                                    daysBeforeStart = daysFromToday(it.startDate),
                                    daysBeforeReturn = daysFromToday(it.endDate)
                                )
                            }
                        }

                        else -> emptyList()
                    }
                }
        }
    }
}