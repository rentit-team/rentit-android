package com.example.rentit.feature.auth

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.moveScreen
import com.example.rentit.feature.auth.component.GoogleLoginButton

@Composable
fun LoginScreen(authViewModel: AuthViewModel, navHostController: NavHostController) {
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
        GoogleLoginButton(authViewModel)
    }
    LoginResultHandler(navHostController, authViewModel)
}

@Composable
fun LoginResultHandler(navHostController: NavHostController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val googleLoginResult = authViewModel.googleLoginResult

    LaunchedEffect(googleLoginResult) {
        googleLoginResult?.let {
            var message = ""
            it.onSuccess { response ->
                val userData = response.data.user
                authViewModel.userData = userData
                if(response.data.isUserRegistered){
                    moveScreen(navHostController, NavigationRoutes.MAIN, isInclusive = true)
                } else {
                    moveScreen(navHostController, NavigationRoutes.JOIN)
                }
                message = "구글 데이터 전송 성공 [${userData.name}/${userData.email}]"
                Log.d("GOOGLE LOGIN SUCCESS", "${response.data}")
            }.onFailure { error ->
                message = "구글 데이터 전송 실패: ${error.message}"
                Log.d("GOOGLE LOGIN FAILED", "${error.message}")
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}