package com.example.rentit.presentation.mypage.myproductsrental.model

import androidx.annotation.StringRes
import com.example.rentit.R

enum class MyProductsRentalFilter(
    @StringRes val titleRes: Int,
    @StringRes val contentDescRes: Int
) {
    WAITING_FOR_RESPONSE(
        R.string.screen_my_products_rental_filter_waiting_for_response,
        R.string.screen_my_products_rental_filter_waiting_for_response_desc
    ),
    WAITING_FOR_SHIPMENT(
        R.string.screen_my_products_rental_filter_waiting_for_shipment,
        R.string.screen_my_products_rental_filter_waiting_for_shipment_desc
    ),
    RENTING(
        R.string.screen_my_products_rental_filter_renting,
        R.string.screen_my_products_rental_filter_renting_desc
    ),
    ACCEPTED(
        R.string.screen_my_products_rental_filter_accepted,
        R.string.screen_my_products_rental_filter_accepted_desc
    ),
    NONE(0, 0)
}