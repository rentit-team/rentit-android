package com.example.rentit.presentation.rentaldetail.common.components.section

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.R
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.rentaldetail.common.model.PriceItemUiModel
import com.example.rentit.common.component.TitledContainer
import com.example.rentit.common.component.PriceSummary

@Composable
fun RentalPaymentSection(
    title: String,
    priceItems: List<PriceItemUiModel>,
    totalLabel: String
) {
    TitledContainer(labelText = AnnotatedString(title)) {
        PriceSummary(
            priceItems = priceItems,
            totalLabel = totalLabel
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    val examplePriceItems = listOf(
        PriceItemUiModel(
            label = stringResource(R.string.screen_rental_detail_renter_paid_price_label_basic_rent),
            price = 30000
        ),
        PriceItemUiModel(
            label = stringResource(R.string.screen_rental_detail_renter_paid_price_label_deposit),
            price = 30000
        )
    )
    RentItTheme {
        RentalPaymentSection(
            title = stringResource(R.string.screen_rental_detail_renter_paid_price_title),
            priceItems = examplePriceItems,
            totalLabel = stringResource(R.string.screen_rental_detail_renter_paid_price_label_total)
        )
    }
}