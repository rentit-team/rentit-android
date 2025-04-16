package com.example.rentit.feature.auth

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.PretendardTextStyle
import com.example.rentit.data.user.GoogleLoginViewModel
import com.example.rentit.data.user.LoginResult

@Composable
fun GoogleLoginButton(viewModel: GoogleLoginViewModel, onLoginSuccess: (String) -> Unit, onError: (String) -> Unit){
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    val loginState by viewModel.loginState.collectAsState()

    // Compose 내부에서 코루틴 사용
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginResult.Success -> onLoginSuccess((loginState as LoginResult.Success).token)
            is LoginResult.Failure -> onError((loginState as LoginResult.Failure).message)
            else -> {}
        }
    }

    OutlinedButton(
        onClick = {
            viewModel.startGoogleLogin(
                context = context,
                activity = activity,
                serverClientId = context.getString(R.string.google_login_client_id)
            )},
        modifier = Modifier.width(250.dp).height(46.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = AppBlack
        ),
        border = BorderStroke(2.dp, Gray200)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .height(20.dp)
                    .padding(end = 12.dp),
                painter = painterResource(id = R.drawable.logo_google),
                contentDescription = stringResource(id = R.string.screen_login_google_logo_description)
            )
            Text(
                text = stringResource(id = R.string.screen_login_google_login_button_text),
                style = PretendardTextStyle.body1_bold
            )
        }
    }
}