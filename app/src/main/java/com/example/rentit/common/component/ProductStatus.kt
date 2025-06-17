package com.example.rentit.common.component

object ProductStatus {
    private const val AVAILABLE = "AVAILABLE"
    private const val RENTED = "RENTED"
    private const val RESERVED = "RESERVED"
    private const val DISABLED = "DISABLED"
    fun getKorProductStatus(enProductStatus: String): String? {
        return when(enProductStatus){
            AVAILABLE -> "대여 가능"
            RENTED -> "대여중"
            RESERVED -> "예약됨"
            DISABLED -> null
            else -> null
        }
    }
}

