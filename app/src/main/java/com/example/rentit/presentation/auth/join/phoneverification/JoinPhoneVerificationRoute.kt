package com.example.rentit.presentation.auth.join.phoneverification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.rentit.common.theme.RentItTheme


@Composable
fun JoinPhoneVerificationRoute() {

    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel: JoinPhoneVerificationViewModel = hiltViewModel()
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    JoinPhoneVerificationSideEffect.NavigateBack -> {
                        // TODO: Navigate back
                    }

                    JoinPhoneVerificationSideEffect.ToastSendCodeFailed -> {
                        // TODO: Toast send code failed
                    }

                    JoinPhoneVerificationSideEffect.ToastTooManyRequests -> {
                        // TODO: Toast too many requests
                    }

                    JoinPhoneVerificationSideEffect.ToastVerificationSuccess -> {
                        // TODO: Toast verification success
                    }

                    JoinPhoneVerificationSideEffect.ToastRequestNotFound -> {
                        // TODO: Toast request not found
                    }
                }
            }
        }
    }

    RentItTheme {
        JoinPhoneVerificationScreen(
            phoneNumber = uiState.phoneNumber,
            code = uiState.code,
            remainingMinutes = uiState.remainingMinutes,
            remainingSeconds = uiState.remainingSeconds,
            isRequestEnabled = uiState.isRequestEnabled,
            isCodeFieldEnabled = uiState.isCodeFieldEnabled,
            isConfirmEnabled = uiState.isConfirmEnabled,
            showRemainingTime = uiState.showRemainingTime,
            showCodeError = uiState.showCodeError,
            onPhoneNumberChange = viewModel::changePhoneNumber,
            onCodeChange = viewModel::changeCode,
            onRequestCode = { },
            onBackPressed = { },
            onConfirm = { },
        )
    }
}