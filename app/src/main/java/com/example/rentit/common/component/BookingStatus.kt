package com.example.rentit.common.component

import androidx.compose.ui.graphics.Color
import com.example.rentit.common.theme.AppGreen
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.SecondaryYellow

enum class BookingStatus(val label: String, val color: Color) {
    PENDING("요청됨", SecondaryYellow),
    ACCEPTED("요청 수락", PrimaryBlue500),
    REJECTED("거절됨", Gray400),
    COMPLETED("거래 완료", AppGreen);

    companion object {
        fun fromLabel(key: String): BookingStatus? {
            return entries.find { it.name == key }
        }

        fun isPending(key: String): Boolean {
            return entries.find { it.name == key } == PENDING
        }
    }
}
