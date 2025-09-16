package com.example.rentit.presentation.mypage.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.navigation.auth.navigateToLogin
import com.example.rentit.presentation.mypage.setting.dialog.LogoutConfirmDialog
import androidx.core.net.toUri
import java.io.File

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
        onCheatingReportFormClick = { openPdf(context) },
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

fun openPdf(context: Context) {
    val file = File(context.cacheDir, "cheating_report_form.pdf")
    context.resources.openRawResource(R.raw.cheating_report_form).use { input ->
        file.outputStream().use { output -> input.copyTo(output) }
    }

    val pdfUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(pdfUri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(intent)
}