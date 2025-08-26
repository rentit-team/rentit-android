package com.example.rentit.navigation.setting

import kotlinx.serialization.Serializable

sealed class SettingRoute {
    @Serializable
    data object Setting: SettingRoute()

    @Serializable
    data object TransactionProof: SettingRoute()
}