package com.example.rentit.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.rentit.navigation.productdetail.navigateToProductDetail

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeRoute(navHostController: NavHostController) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val productListResult by homeViewModel.productList.collectAsState()

    val productList = productListResult?.getOrNull()?.products ?: emptyList()

    LaunchedEffect(Unit) {
        homeViewModel.getProductList()
    }

    HomeScreen(
        sortedProducts = productList.reversed(),
        onProductClick = { navHostController.navigateToProductDetail(it) }
    )
}