package com.example.rentit.feature.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonTextField
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.moveScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.AppRed
import com.example.rentit.common.theme.PretendardTextStyle
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@Composable
fun JoinScreen(authViewModel: AuthViewModel, navHostController: NavHostController) {
    val nickname = authViewModel.nickname
    var isButtonClicked by remember { mutableStateOf(false) }
    Column {
        CommonTopAppBar(
            title = stringResource(id = R.string.screen_join_title),
            onClick = {}
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .screenHorizontalPadding()
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.weight(0.5f))
            HighlightedHeadline()
            CommonTextField(value = TextFieldValue(nickname), onValueChange = { authViewModel.onNicknameChanged(it) }, placeholder = stringResource(R.string.app_name))
            if(nickname.isBlank() && isButtonClicked){
                Text(
                    modifier = Modifier.padding(top = 6.dp, start = 6.dp),
                    text = stringResource(id = R.string.screen_join_nickname_empty_notification),
                    color = AppRed,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            CommonButton(stringResource(R.string.screen_join_btn_text), PrimaryBlue500, Color.White) { signUp(authViewModel, navHostController) { isButtonClicked = true } }
        }
    }
    SignUpResultHandler(authViewModel, navHostController)
}


@Composable
fun HighlightedHeadline() {
    Text(
        modifier = Modifier.padding(bottom = 27.dp),
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = PrimaryBlue500)) {
                append(stringResource(R.string.app_name))
            }
            append(stringResource(R.string.screen_join_headline),)
        },
        style = PretendardTextStyle.headline_bold,
        textAlign = TextAlign.Start
    )
}

@Composable
fun SignUpResultHandler(authViewModel: AuthViewModel, navHostController: NavHostController) {
    val context = LocalContext.current
    val signUpResult = authViewModel.signUpResult

    LaunchedEffect(signUpResult) {
        signUpResult?.let {
            var message = ""
            it.onSuccess { response ->
                moveScreen(navHostController, NavigationRoutes.LOGIN)
                message = "회원가입 성공"
                Log.d("SIGN UP SUCCESS", "$response")
            }.onFailure {error ->
                moveScreen(navHostController, NavigationRoutes.LOGIN)
                message = "회원가입 실패: ${error.message}"
                Log.d("SIGN UP FAILED", "${error.message}")
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}

fun signUp(authViewModel: AuthViewModel, navHostController: NavHostController, onNicknameEmpty: () -> Unit) {
    val userData = authViewModel.userData
    val nickname = authViewModel.nickname

    if(nickname.isNotEmpty()){
        if(userData != null){
            authViewModel.onSignUp(
                name = userData.name,
                email = userData.email,
                nickname = nickname)
        } else {
            moveScreen(navHostController, NavigationRoutes.LOGIN, isInclusive = true)
        }
    } else {
        onNicknameEmpty()
    }
}

@Preview
@Composable
fun JoinPreview(){
    RentItTheme {
        JoinScreen(hiltViewModel(), rememberNavController())
    }
}