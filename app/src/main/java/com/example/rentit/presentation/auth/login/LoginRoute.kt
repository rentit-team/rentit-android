package com.example.rentit.presentation.auth.login

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.BuildConfig
import com.example.rentit.navigation.auth.navigateToJoinPhoneVerification
import com.example.rentit.navigation.bottomtab.navigateToHome
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun LoginRoute(navHostController: NavHostController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current
    val launcher = googleSignInLauncher(viewModel)

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    LoginSideEffect.NavigateToHome -> {
                        navHostController.navigateToHome()
                    }
                    is LoginSideEffect.NavigateToJoin -> {
                        navHostController.navigateToJoinPhoneVerification(sideEffect.name, sideEffect.email)
                    }
                    LoginSideEffect.ToastGoogleSignInError -> {
                        Toast.makeText(context, "구글 로그인 중 오류가 발생했어요.", Toast.LENGTH_SHORT).show()
                    }
                    LoginSideEffect.ToastGoogleSignInFailed -> {
                        Toast.makeText(context, "구글 로그인에 실패했어요. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    is LoginSideEffect.ToastGreetingMessage -> {
                        Toast.makeText(context, "반가워요, ${sideEffect.name}님!", Toast.LENGTH_SHORT).show()
                    }
                    LoginSideEffect.ToastLoginFailed -> {
                        Toast.makeText(context, "로그인에 실패했어요. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    LoginScreen { launchGoogleSignIn(launcher, context) }
}

@Composable
fun googleSignInLauncher(loginViewModel: LoginViewModel): ManagedActivityResultLauncher<Intent, ActivityResult> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        loginViewModel.handleGoogleSignInResult(
            data = result.data
        )
    }
}

fun launchGoogleSignIn(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>, context: Context) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
        .requestServerAuthCode(BuildConfig.GOOGLE_CLIENT_ID)
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    launcher.launch(googleSignInClient.signInIntent)    // Google Login Intent 실행
}