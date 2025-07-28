package com.example.rentit.common.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun parseLocalDateOrNull(dateStr: String): LocalDate? {
    return try {
        LocalDate.parse(dateStr)
    } catch (e: Exception) {
        null
    }
}