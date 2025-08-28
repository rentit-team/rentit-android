package com.example.rentit.presentation.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonBorders
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.PretendardTextStyle
import com.example.rentit.common.theme.RentItTheme


@Composable
fun LoginScreen(onGoogleLoginClick: () -> Unit = {}) {
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
        GoogleLoginButton(onGoogleLoginClick)
    }
}


@Composable
fun GoogleLoginButton(onGoogleLoginClick: () -> Unit){
    OutlinedButton(
        onClick = onGoogleLoginClick,
        modifier = Modifier.width(250.dp).height(46.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = AppBlack
        ),
        border = CommonBorders.mediumBorder()
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
                text = stringResource(id = R.string.screen_login_google_login_btn_text),
                style = PretendardTextStyle.body1_bold
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    RentItTheme {
        LoginScreen()
    }
}