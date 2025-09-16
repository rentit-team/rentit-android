package com.example.rentit.presentation.mypage.setting

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
import com.example.rentit.navigation.auth.navigateToLogin
import com.example.rentit.presentation.mypage.setting.dialog.LogoutConfirmDialog

@Composable
fun SettingRoute(navHostController: NavHostController) {
    val viewModel: SettingViewModel = hiltViewModel()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    is SettingSideEffect.NavigateToLogin -> {
                        navHostController.navigateToLogin()
                    }
                    is SettingSideEffect.ToastLogoutComplete -> {
                        Toast.makeText(context, R.string.toast_logout_complete, Toast.LENGTH_SHORT).show()
                    }
                    SettingSideEffect.ToastComingSoon -> {
                        Toast.makeText(context, context.getString(R.string.common_toast_feat_coming_soon), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    SettingScreen(
        onBackPressed = navHostController::popBackStack,
        onRegisterAccountClick = viewModel::showComingSoonMessage,
        onCheatingReportFormClick = { },
        onKeywordAlertSettingClick = viewModel::showComingSoonMessage,
        onSafetyGuideClick = viewModel::showComingSoonMessage,
        onLogoutClick = viewModel::showLogoutConfirmDialog
    )

    if(uiState.showLogoutConfirmDialog) {
        LogoutConfirmDialog(
            onDismiss = viewModel::hideLogoutConfirmDialog,
            onConfirm = viewModel::onLogoutConfirmed
        )
    }
}