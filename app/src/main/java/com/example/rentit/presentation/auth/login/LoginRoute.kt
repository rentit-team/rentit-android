package com.example.rentit.presentation.auth.login

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.BuildConfig
import com.example.rentit.data.user.model.GoogleSignInResult
import com.example.rentit.navigation.auth.navigateToJoinPhoneVerification
import com.example.rentit.navigation.bottomtab.navigateToHome
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.net.URLEncoder

private const val TAG = "Login"

@Composable
fun LoginRoute(navHostController: NavHostController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current
    val launcher = googleSignInLauncher(viewModel)

    GoogleSignInStateHandler(viewModel)
    LoginResultHandler(navHostController, viewModel)

    LoginScreen { launchGoogleSignIn(launcher, context) }
}


@Composable
fun GoogleSignInStateHandler(loginViewModel: LoginViewModel) {
    val googleSignInState by loginViewModel.googleSignInState.collectAsStateWithLifecycle()

    LaunchedEffect(googleSignInState) {
        when (googleSignInState) {
            is GoogleSignInResult.Success -> {
                var authCode = (googleSignInState as GoogleSignInResult.Success).authCode
                authCode = URLEncoder.encode(authCode, "UTF-8")
                Log.d("GoogleSignInResult", "성공: $authCode")
                loginViewModel.onGoogleLogin(authCode)
            }

            is GoogleSignInResult.Failure -> {
                val errorMessage = (googleSignInState as GoogleSignInResult.Failure).message
                Log.d("GoogleSignInResult", "실패: $errorMessage")
            }

            else -> Unit
        }
    }
}

@Composable
fun LoginResultHandler(navHostController: NavHostController, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    val googleLoginResult by loginViewModel.googleLoginResult.collectAsStateWithLifecycle()
    val userData by loginViewModel.userData.collectAsStateWithLifecycle()

    LaunchedEffect(googleLoginResult) {
        googleLoginResult?.onSuccess { response ->
            if(response.data.isUserRegistered){
                navHostController.navigateToHome()
                loginViewModel.saveTokenFromPrefs(response.data.accessToken.token)
            } else {
                navHostController.navigateToJoinPhoneVerification(userData?.name, userData?.email)
            }
            Toast.makeText(context, "구글 데이터 전송 성공 [${userData?.name}/${userData?.email}]", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "${response.data}")
        }?.onFailure { error ->

            Toast.makeText(context, "구글 데이터 전송 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "${error.message}")
        }
    }
}



@Composable
fun googleSignInLauncher(loginViewModel: LoginViewModel): ManagedActivityResultLauncher<Intent, ActivityResult> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        loginViewModel.handleGoogleSignInResult(
            requestCode = 9001,
            resultCode = result.resultCode,
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