package com.example.rentit.presentation.home

data class HomeState(
    val isLoading: Boolean = false,
    val showServerErrorDialog: Boolean = false,
    val showNetworkErrorDialog: Boolean = false,
)