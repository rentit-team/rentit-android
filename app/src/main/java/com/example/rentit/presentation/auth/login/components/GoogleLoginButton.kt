package com.example.rentit.presentation.auth.login.components

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rentit.BuildConfig
import com.example.rentit.R
import com.example.rentit.common.component.CommonBorders
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.PretendardTextStyle
import com.example.rentit.presentation.auth.login.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun GoogleLoginButton(loginViewModel: LoginViewModel){
    val context = LocalContext.current
    val launcher = googleSignInLauncher(loginViewModel)

    OutlinedButton(
        onClick = { launchGoogleSignIn(launcher, context) },
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