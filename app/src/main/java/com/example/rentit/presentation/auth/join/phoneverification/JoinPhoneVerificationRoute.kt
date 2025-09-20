package com.example.rentit.presentation.auth.join.phoneverification

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.navigation.auth.navigateToJoinNickname


@Composable
fun JoinPhoneVerificationRoute(navHostController: NavHostController, name: String, email: String) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val viewModel: JoinPhoneVerificationViewModel = hiltViewModel()
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    JoinPhoneVerificationSideEffect.NavigateBack -> {
                        navHostController.popBackStack()
                    }

                    JoinPhoneVerificationSideEffect.NavigateToNickname -> {
                        navHostController.navigateToJoinNickname(name, email)
                    }

                    JoinPhoneVerificationSideEffect.ToastSendCodeFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_send_code_fail), Toast.LENGTH_SHORT).show()
                    }

                    JoinPhoneVerificationSideEffect.ToastTooManyRequests -> {
                        Toast.makeText(context, context.getString(R.string.toast_too_many_code_request), Toast.LENGTH_SHORT).show()
                    }

                    JoinPhoneVerificationSideEffect.ToastVerificationSuccess -> {
                        Toast.makeText(context, context.getString(R.string.toast_phone_verification_success), Toast.LENGTH_SHORT).show()
                    }

                    JoinPhoneVerificationSideEffect.ToastVerificationFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_phone_verification_failed), Toast.LENGTH_SHORT).show()
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
            errorMessageRes = uiState.errorMessageRes,
            isRequestEnabled = uiState.isRequestEnabled,
            isConfirmEnabled = uiState.isConfirmEnabled,
            showRemainingTime = uiState.showRemainingTime,
            onPhoneNumberChange = viewModel::changePhoneNumber,
            onCodeChange = viewModel::changeCode,
            onRequestCode = viewModel::requestCode,
            onBackPressed = viewModel::navigateBack,
            onConfirm = viewModel::codeConfirm,
        )
    }
}