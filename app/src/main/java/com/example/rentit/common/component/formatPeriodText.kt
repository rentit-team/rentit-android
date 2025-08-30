package com.example.rentit.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rentit.R

@Composable
fun formatPeriodText(minPeriod: Int?, maxPeriod: Int?): String {
    return when {
        minPeriod != null && maxPeriod != null -> {
            stringResource(
                R.string.product_list_item_period_text_more_and_less_than_day,
                minPeriod.toInt(),
                maxPeriod.toInt()
            )
        }
        minPeriod != null -> {
            stringResource(R.string.product_list_item_period_text_more_than_day, minPeriod)
        }
        maxPeriod != null -> {
            stringResource(R.string.product_list_item_period_text_less_than_day, maxPeriod)
        }
        else -> stringResource(R.string.product_list_item_period_text_more_than_zero)
    }
}