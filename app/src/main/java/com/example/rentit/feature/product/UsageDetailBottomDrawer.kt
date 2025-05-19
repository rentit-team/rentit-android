package com.example.rentit.feature.product

import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.common.theme.RentItTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsageDetailBottomDrawer() {
    val dateRangePickerState = rememberDateRangePickerState()
    DateRangePicker(state = dateRangePickerState)
}

@Preview(showBackground = true)
@Composable
fun PreviewUsageDetailBottomDrawer() {
    RentItTheme {
        UsageDetailBottomDrawer()
    }
}