package com.example.rentit.presentation.rentaldetail.model

data class RequestAcceptDialogUiModel(
    val startDate: String,
    val endDate: String,
    val expectedRevenue: Int
)