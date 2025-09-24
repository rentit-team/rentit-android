package com.example.rentit.common.component.formatter

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R

@Composable
fun formatPeriodText(minPeriod: Int?, maxPeriod: Int?): String {
    return when {
        minPeriod != null && maxPeriod != null -> {
            stringResource(
                R.string.common_rental_period_min_max,
                minPeriod,
                maxPeriod
            )
        }
        minPeriod != null -> {
            stringResource(R.string.common_rental_period_min, minPeriod)
        }
        maxPeriod != null -> {
            stringResource(R.string.common_rental_period_max, maxPeriod)
        }
        else -> stringResource(R.string.common_rental_period_default)
    }
}

@Composable
fun formatPeriodTextWithLabel(minPeriod: Int?, maxPeriod: Int?): String {
    return when {
        minPeriod != null && maxPeriod != null -> {
            stringResource(
                R.string.common_rental_period_min_max_label,
                minPeriod,
                maxPeriod
            )
        }
        minPeriod != null -> {
            stringResource(R.string.common_rental_period_min_label, minPeriod)
        }
        maxPeriod != null -> {
            stringResource(R.string.common_rental_period_max_label, maxPeriod)
        }
        else -> stringResource(R.string.common_rental_period_default_label)
    }
}