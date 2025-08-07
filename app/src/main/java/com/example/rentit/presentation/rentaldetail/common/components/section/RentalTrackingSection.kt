package com.example.rentit.presentation.rentaldetail.common.components.section

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.component.TitledContainer
import com.example.rentit.presentation.rentaldetail.common.components.LabeledValue

@Composable
fun RentalTrackingSection(rentalTrackingNumber: String? = null, returnTrackingNumber: String? = null) {
    TitledContainer(labelText = AnnotatedString(stringResource(R.string.screen_rental_detail_tracking_info_title))) {
        LabeledValue(
            modifier = Modifier.padding(bottom = 10.dp),
            labelText = stringResource(R.string.screen_rental_detail_rental_tracking_num),
            value = rentalTrackingNumber
                ?: stringResource(R.string.screen_rental_detail_tracking_num_unregistered)
        )
        LabeledValue(
            labelText = stringResource(R.string.screen_rental_detail_returned_tracking_num),
            value = returnTrackingNumber
                ?: stringResource(R.string.screen_rental_detail_tracking_num_unregistered)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    RentItTheme {
        RentalTrackingSection(
            rentalTrackingNumber = null,
            returnTrackingNumber = null
        )
    }
}