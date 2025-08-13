package com.example.rentit.presentation.auth.join.nickname

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.navigation.auth.navigateToLogin

private const val TAG = "Join"

@Composable
fun JoinNicknameRoute(navHostController: NavHostController, name: String?, email: String?) {

    val joinNicknameViewModel: JoinNicknameViewModel = hiltViewModel()
    var nickname by remember { mutableStateOf("") }
    var showNicknameBlankError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(email) {
        if(email == null) {
            Toast.makeText(context, context.getString(R.string.screen_join_error_email_null), Toast.LENGTH_SHORT).show()
            navHostController.popBackStack()
        }
    }

    SignUpResultHandler(joinNicknameViewModel, navHostController)

    JoinNicknameScreen(
        nickname = nickname,
        showNicknameBlankError = showNicknameBlankError,
        onBackPressed = { navHostController.popBackStack() },
        onNicknameChange = { nickname = it },
        onCompleteClick = {
            if (nickname.isBlank()) {
                showNicknameBlankError = true
            } else if (email != null) {
                joinNicknameViewModel.onSignUp(name ?: "", email, nickname)
            }
        }
    )
}



@Composable
private fun SignUpResultHandler(joinNicknameViewModel: JoinNicknameViewModel, navHostController: NavHostController) {
    val context = LocalContext.current
    val signUpResult = joinNicknameViewModel.signUpResult.collectAsStateWithLifecycle()

    LaunchedEffect(signUpResult) {
        signUpResult.value?.onSuccess {
            Log.d(TAG, "Sign Up Success")
            Toast.makeText(context, context.getString(R.string.screen_join_toast_complete), Toast.LENGTH_SHORT).show()
            navHostController.navigateToLogin()
        }?.onFailure { error ->
            Log.d(TAG, "${error.message}")
            Toast.makeText(context, context.getString(R.string.screen_join_toast_fail), Toast.LENGTH_SHORT).show()
            navHostController.navigateToLogin()
        }

    }
}