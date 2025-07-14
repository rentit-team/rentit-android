package com.example.rentit.feature.auth.join

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

private const val TAG = "Join"

@Composable
fun JoinScreen(navHostController: NavHostController, name: String?, email: String?) {

    val joinViewModel: JoinViewModel = hiltViewModel()
    val nickname = remember { mutableStateOf("") }
    val isButtonClicked = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(email) {
        if(email == null) {
            Toast.makeText(
                context,
                context.getString(R.string.screen_join_error_email_null),
                Toast.LENGTH_SHORT
            ).show()
            navHostController.popBackStack()
        }
    }

    SignUpResultHandler(joinViewModel, navHostController)

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
            CommonTextField(
                value = TextFieldValue(nickname.value),
                onValueChange = { nickname.value = it },
                placeholder = stringResource(R.string.app_name)
            )
            if (nickname.value.isBlank() && isButtonClicked.value) {
                Text(
                    modifier = Modifier.padding(top = 6.dp, start = 6.dp),
                    text = stringResource(id = R.string.screen_join_nickname_empty_notification),
                    color = AppRed,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            CommonButton(
                stringResource(R.string.screen_join_btn_text),
                PrimaryBlue500,
                Color.White
            ) {
                if (nickname.value.isEmpty()) {
                    isButtonClicked.value = true
                } else if(email == null){
                    navHostController.popBackStack()
                } else {
                    joinViewModel.onSignUp(name ?: "", email, nickname.value)
                }
            }
        }
    }
}


@Composable
fun HighlightedHeadline() {
    Text(
        modifier = Modifier.padding(bottom = 27.dp),
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = PrimaryBlue500)) {
                append(stringResource(R.string.app_name))
            }
            append(stringResource(R.string.screen_join_headline))
        },
        style = PretendardTextStyle.headline_bold,
        textAlign = TextAlign.Start
    )
}

@Composable
fun SignUpResultHandler(joinViewModel: JoinViewModel, navHostController: NavHostController) {
    val context = LocalContext.current
    val signUpResult = joinViewModel.signUpResult.collectAsStateWithLifecycle()

    LaunchedEffect(signUpResult) {
        signUpResult.value?.onSuccess {
            Log.d(TAG, "Sign Up Success")
            Toast.makeText(context, context.getString(R.string.screen_join_toast_complete), Toast.LENGTH_SHORT).show()
            moveScreen(navHostController, NavigationRoutes.LOGIN)
        }?.onFailure { error ->
            Log.d(TAG, "${error.message}")
            Toast.makeText(context, context.getString(R.string.screen_join_toast_fail), Toast.LENGTH_SHORT).show()
            moveScreen(navHostController, NavigationRoutes.LOGIN)
        }

    }
}

@Preview
@Composable
fun JoinPreview() {
    RentItTheme {
        JoinScreen(rememberNavController(), "", "")
    }
}