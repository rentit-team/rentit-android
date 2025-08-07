package com.example.rentit.presentation.rentaldetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.presentation.rentaldetail.renter.RentalDetailRenterScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalDetailRoute(navHostController: NavHostController) {

    val viewModel: RentalDetailViewModel = hiltViewModel()
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    RentalDetailRenterScreen(navHostController, uiModel, scrollState)
}