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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rentit.R
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.feature.auth.component.GoogleLoginButton

@Composable
fun LoginScreen() {
    RentItTheme {
        Login()
    }

}

@Composable
fun Login(){
    val context = LocalContext.current
    val authViewModel: AuthViewModel = hiltViewModel()
    val loginResult = authViewModel.googleLoginResult

    LaunchedEffect(loginResult) {
        loginResult?.let {
            if (it.isSuccess) {
                val user = it.getOrNull()?.body()?.data?.user
                val message = "구글 데이터 서버 전송 성공 [${user?.name}/${user?.email}]"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                Log.d("GOOGLE LOGIN SUCCESS", "${it.getOrNull()?.body()?.data}")
            } else {
                Log.d("GOOGLE LOGIN FAILED", "${it.exceptionOrNull()?.message}")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.width(140.dp).padding(bottom = 10.5.dp),
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = stringResource(id = R.string.screen_login_app_logo_description)
        )
        Text(
            modifier = Modifier.padding(bottom = 64.dp),
            text = stringResource(id = R.string.screen_login_label),
            style = MaterialTheme.typography.labelMedium
        )
        GoogleLoginButton({authCode -> authViewModel.onGoogleLogin(authCode)}, { errorMsg  -> Log.d("ErrorMsg", "$errorMsg") })
    }
}


@Preview
@Composable
fun LoginPreview(){
    LoginScreen()
}