package com.example.rentit.presentation.pay

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayRoute(navHostController: NavHostController, payInfo: PayUiModel) {

    val viewModel: PayViewModel = hiltViewModel()
    val isDialogVisible by viewModel.isDialogVisible.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    PayScreen(
        payModel = payInfo,
        isDialogVisible = isDialogVisible,
        scrollState = scrollState,
        onBackClick = { navHostController.popBackStack() },
        onPayClick = { viewModel.setDialogVisibility(true) },
        onDialogClose = { viewModel.setDialogVisibility(false) },
        onDialogConfirm = { viewModel.setDialogVisibility(false) }
    )
}