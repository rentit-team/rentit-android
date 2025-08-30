package com.example.rentit.common.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toShortFormat(): String =
    this.format(DateTimeFormatter.ofPattern("yy.MM.dd"))

@RequiresApi(Build.VERSION_CODES.O)
fun OffsetDateTime.toShortFormat(): String =
    this.format(DateTimeFormatter.ofPattern("yy.MM.dd"))