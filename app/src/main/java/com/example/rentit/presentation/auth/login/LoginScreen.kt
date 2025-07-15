package com.example.rentit.presentation.auth.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.common.navigation.NavigationRoutes
import com.example.rentit.common.navigation.moveScreen
import com.example.rentit.common.storage.saveToken
import com.example.rentit.data.user.model.GoogleSignInResult
import com.example.rentit.presentation.auth.login.components.GoogleLoginButton
import java.net.URLEncoder

private const val TAG = "Login"

@Composable
fun LoginScreen(navHostController: NavHostController) {
    val loginViewModel: LoginViewModel = hiltViewModel()

    GoogleSignInStateHandler(loginViewModel)
    LoginResultHandler(navHostController, loginViewModel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .width(140.dp)
                .padding(bottom = 10.5.dp),
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = stringResource(id = R.string.screen_login_app_logo_description)
        )
        Text(
            modifier = Modifier.padding(bottom = 64.dp),
            text = stringResource(id = R.string.screen_login_label),
            style = MaterialTheme.typography.labelMedium
        )
        GoogleLoginButton(loginViewModel)
    }
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
                moveScreen(navHostController, NavigationRoutes.MAIN, isInclusive = true)
                saveToken(context, response.data.accessToken.token)
            } else {
                moveScreen(navHostController, NavigationRoutes.JOIN + "/${userData?.name}/${userData?.email}")
            }
            Toast.makeText(context, "구글 데이터 전송 성공 [${userData?.name}/${userData?.email}]", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "${response.data}")
        }?.onFailure { error ->

            Toast.makeText(context, "구글 데이터 전송 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "${error.message}")
        }
    }
}