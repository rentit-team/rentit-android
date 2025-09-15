package com.example.rentit.presentation.main

data class MainState(
    val showSessionExpiredDialog: Boolean = false,
    val showNetworkErrorDialog: Boolean = false,
    val showServerErrorDialog: Boolean = false
)