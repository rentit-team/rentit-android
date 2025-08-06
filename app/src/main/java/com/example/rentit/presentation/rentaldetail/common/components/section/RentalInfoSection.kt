package com.example.rentit.presentation.rentaldetail.common.components.section

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.rentaldetail.common.components.LabeledSection
import com.example.rentit.presentation.rentaldetail.common.components.RentalSummary
import com.example.rentit.presentation.rentaldetail.common.model.RentalInfoUiModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalInfoSection(
    title: AnnotatedString,
    titleColor: Color,
    rentalInfo: RentalInfoUiModel,
    content: @Composable () -> Unit = {},
) {
    LabeledSection(
        labelText = title,
        labelColor = titleColor
    ) {
        RentalSummary(
            productTitle = rentalInfo.productTitle,
            thumbnailImgUrl = rentalInfo.thumbnailImgUrl,
            startDate = rentalInfo.startDate,
            endDate = rentalInfo.endDate,
            totalPrice = rentalInfo.totalPrice,
        )
        content()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val exampleRentalInfo = RentalInfoUiModel(
        productTitle = "캐논 EOS 550D",
        thumbnailImgUrl = "",
        startDate = "2025-08-17",
        endDate = "2025-08-20",
        totalPrice = 10_000 * 6
    )
    RentItTheme {
        RentalInfoSection(
            title = AnnotatedString("대여 요청"),
            titleColor = Color.Black,
            rentalInfo = exampleRentalInfo
        )
    }
}