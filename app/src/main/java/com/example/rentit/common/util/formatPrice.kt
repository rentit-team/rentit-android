package com.example.rentit.common.util

import java.text.NumberFormat

fun formatPrice(price: Int): String = NumberFormat.getNumberInstance().format(price)