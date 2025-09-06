package com.example.rentit.presentation.rentaldetail.components.section

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
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray400
import com.example.rentit.presentation.rentaldetail.components.LabeledValue

@Composable
fun RentalTrackingSection(
    rentalTrackingNumber: String? = null,
    rentalCourierName: String? = null ,
    returnTrackingNumber: String? = null,
    returnCourierName: String? = null
) {
    val rentalTrackingNumText = rentalTrackingNumber
        ?: stringResource(R.string.screen_rental_detail_tracking_num_unregistered)
    val returnTrackingNumText = returnTrackingNumber
        ?: stringResource(R.string.screen_rental_detail_tracking_num_unregistered)

    val unRegisteredTextColor = Gray400

    TitledContainer(labelText = AnnotatedString(stringResource(R.string.screen_rental_detail_tracking_info_title))) {
        LabeledValue(
            modifier = Modifier.padding(bottom = 10.dp),
            labelText = stringResource(R.string.screen_rental_detail_rental_tracking_num),
            value = "${rentalCourierName.orEmpty()}  $rentalTrackingNumText",
            valueTextColor = if(rentalTrackingNumber == null) unRegisteredTextColor else AppBlack
        )
        LabeledValue(
            labelText = stringResource(R.string.screen_rental_detail_returned_tracking_num),
            value = "${returnCourierName.orEmpty()}  $returnTrackingNumText",
            valueTextColor = if(returnTrackingNumber == null) unRegisteredTextColor else AppBlack
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    RentItTheme {
        RentalTrackingSection(
            rentalCourierName = "롯데택배",
            rentalTrackingNumber = "1012456879",
            returnCourierName = null,
            returnTrackingNumber = null,
        )
    }
}