package com.example.rentit.presentation.rentaldetail.common.components.section

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.rentaldetail.common.components.LabeledSection
import com.example.rentit.presentation.rentaldetail.common.components.RentalSummary
import com.example.rentit.presentation.rentaldetail.common.model.RentalSummaryUiModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalInfoSection(
    title: String,
    titleColor: Color,
    subTitle: String? = null,
    subTitleColor: Color = Gray400,
    rentalInfo: RentalSummaryUiModel,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val titleWithSubTitle = buildAnnotatedString {
        append(title)
        if(!subTitle.isNullOrEmpty())
            withStyle(
                style = SpanStyle(
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    color = subTitleColor
                )
            ) { append(" $subTitle") }
    }

    LabeledSection(
        labelText = titleWithSubTitle,
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
    val exampleRentalInfo = RentalSummaryUiModel(
        productTitle = "캐논 EOS 550D",
        thumbnailImgUrl = "",
        startDate = "2025-08-17",
        endDate = "2025-08-20",
        totalPrice = 10_000 * 6
    )
    RentItTheme {
        RentalInfoSection(
            title = "대여 요청",
            titleColor = Color.Black,
            subTitle = "대여 3일전",
            rentalInfo = exampleRentalInfo
        )
    }
}