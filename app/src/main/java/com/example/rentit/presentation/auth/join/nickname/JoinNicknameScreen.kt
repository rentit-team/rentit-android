package com.example.rentit.presentation.auth.join.nickname

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.AppRed
import com.example.rentit.common.theme.PretendardTextStyle
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@Composable
fun JoinNicknameScreen(
    nickname: String,
    showNicknameBlankError: Boolean,
    onBackPressed: () -> Unit,
    onNicknameChange: (String) -> Unit,
    onCompleteClick: () -> Unit
) {
    Column {
        CommonTopAppBar(
            title = stringResource(id = R.string.screen_join_title),
        ) { onBackPressed() }
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
                value = nickname,
                onValueChange = onNicknameChange,
                placeholder = stringResource(R.string.app_name)
            )
            if (nickname.isBlank() && showNicknameBlankError) {
                Text(
                    modifier = Modifier.padding(top = 6.dp, start = 6.dp),
                    text = stringResource(id = R.string.screen_join_nickname_empty_notification),
                    color = AppRed,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            CommonButton(
                text = stringResource(R.string.screen_join_btn_text),
                containerColor = PrimaryBlue500,
                contentColor = Color.White
            ) { onCompleteClick() }
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
