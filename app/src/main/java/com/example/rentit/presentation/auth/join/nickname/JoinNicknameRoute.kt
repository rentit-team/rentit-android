package com.example.rentit.presentation.auth.join.nickname

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
import com.example.rentit.common.ui.component.layout.RentItLoadingScreen
import com.example.rentit.navigation.auth.navigateToLogin

@Composable
fun JoinNicknameRoute(navHostController: NavHostController, name: String, email: String) {

    val viewModel: JoinNicknameViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when (it) {
                    JoinNicknameSideEffect.NavigateToHome -> {
                        navHostController.navigateToLogin()
                    }
                    JoinNicknameSideEffect.JoinNicknameSuccess -> {
                        Toast.makeText(context, R.string.screen_join_nickname_toast_complete, Toast.LENGTH_SHORT).show()
                    }
                    JoinNicknameSideEffect.JoinNicknameError -> {
                        Toast.makeText(context, R.string.screen_join_nickname_toast_fail, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    JoinNicknameScreen(
        nickname = uiState.nickname,
        showNicknameBlankError = uiState.showNicknameBlankError,
        buttonEnabled = uiState.buttonEnabled,
        onBackPressed = navHostController::popBackStack,
        onNicknameChange = viewModel::updateNickname,
        onCompleteClick = { viewModel.onSignUp(name, email) }
    )

    RentItLoadingScreen(uiState.isLoading)
}