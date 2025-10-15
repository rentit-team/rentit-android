package com.example.rentit.presentation.rentaldetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.ui.component.item.RentItDivider
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.ui.formatter.priceFormatter
import com.example.rentit.presentation.rentaldetail.model.PriceSummaryUiModel

/**
 * 비용 내역과 총합을 나타내는 UI 컴포넌트
 */

private val priceItemBottomPadding = 10.dp
private val dividerTopPadding = 8.dp
private val dividerBottomPadding = 14.dp

@Composable
fun PriceSummary(
    modifier: Modifier = Modifier,
    priceItems: List<PriceSummaryUiModel>,
    totalLabel: String,
) {
    val totalPrice = priceItems.sumOf { it.price }

    Column(modifier) {

        priceItems.forEach {
            LabeledValue(
                modifier = Modifier.padding(bottom = priceItemBottomPadding),
                labelText = it.label,
                value = "${priceFormatter(it.price)} ${stringResource(R.string.common_price_unit)}"
            )
        }

        RentItDivider(modifier.padding(top = dividerTopPadding, bottom = dividerBottomPadding))

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = totalLabel,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "${priceFormatter(totalPrice)} 원",
                style = MaterialTheme.typography.labelLarge,
                color = PrimaryBlue500
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PriceSummaryPreview() {
    val priceItems = listOf(
        PriceSummaryUiModel(label = "기본 대여료", price = 30_000),
        PriceSummaryUiModel(label = "지연 보상금 (2일)", price = 60_000)
    )
    RentItTheme {
        PriceSummary(
            priceItems = priceItems,
            totalLabel = "총합"
        )
    }
}