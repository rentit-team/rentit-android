package com.example.rentit.navigation.myproductsrental

import kotlinx.serialization.Serializable

sealed class MyProductsRentalRoute {
    @Serializable
    data object MyProductsRental : MyProductsRentalRoute()
}