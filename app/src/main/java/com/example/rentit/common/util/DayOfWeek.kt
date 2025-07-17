package com.example.rentit.common.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek

private val korLabels = listOf("월", "화", "수", "목", "금", "토", "일")

@RequiresApi(Build.VERSION_CODES.O)
fun DayOfWeek.getKorLabel(): String = korLabels[this.ordinal]