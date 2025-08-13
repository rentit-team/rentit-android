package com.example.rentit.presentation.auth.join.nickname

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonTextField
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.paddingForBottomBarButton
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.PretendardTextStyle
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.auth.join.components.InputErrorMessage

@Composable
fun JoinNicknameScreen(
    nickname: String,
    showNicknameBlankError: Boolean,
    onBackPressed: () -> Unit,
    onNicknameChange: (String) -> Unit,
    onCompleteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(id = R.string.screen_join_title),
            ) { onBackPressed() }
        },
        bottomBar = {
            CommonButton(
                modifier = Modifier
                    .screenHorizontalPadding()
                    .paddingForBottomBarButton(),
                text = stringResource(R.string.screen_join_btn_text),
                containerColor = PrimaryBlue500,
                contentColor = Color.White
            ) { onCompleteClick() }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .screenHorizontalPadding()
                .padding(it),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            HighlightedHeadline()

            CommonTextField(
                value = nickname,
                onValueChange = onNicknameChange,
                placeholder = stringResource(R.string.app_name)
            )

            if (nickname.isBlank() && showNicknameBlankError) {
                InputErrorMessage( stringResource(id = R.string.screen_join_nickname_empty_notification))
            }

            Spacer(modifier = Modifier.weight(1f))

        }
    }
}

@Composable
private fun HighlightedHeadline() {
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

@Preview
@Composable
fun JoinNicknameScreenPreview() {
    RentItTheme {
        JoinNicknameScreen(
            nickname = "",
            showNicknameBlankError = true,
            onNicknameChange = {},
            onBackPressed = {},
            onCompleteClick = {}
        )
    }
}
