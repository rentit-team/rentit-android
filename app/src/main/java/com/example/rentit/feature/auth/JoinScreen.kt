package com.example.rentit.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonTextField
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.PretendardTextStyle
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@Composable
fun JoinScreen() {
    RentItTheme {
        Column {
            CommonTopAppBar(
                title = stringResource(id = R.string.screen_join_title),
                onClick = {}
            )
            JoinContent(
                headlinePrefix = stringResource(R.string.app_name),
                headlineSuffix = stringResource(R.string.screen_join_headline),
                placeholder = stringResource(R.string.app_name),
                buttonText = stringResource(R.string.screen_join_button_text),
                onClick = {}
            )
        }
    }
}

@Composable
fun JoinContent(
    headlinePrefix: String,
    headlineSuffix: String,
    placeholder: String,
    buttonText: String,
    onClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .screenHorizontalPadding()
            .padding(bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.5f))

        HighlightedHeadline(headlinePrefix, headlineSuffix, Modifier.align(Alignment.Start))

        CommonTextField(onValueChange = {}, placeholder = placeholder)

        Spacer(modifier = Modifier.weight(1f))

        CommonButton(buttonText, onClick)
    }
}

@Composable
fun HighlightedHeadline(brandName: String, headline: String, modifier: Modifier) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = PrimaryBlue500)) {
                append(brandName)
            }
            append(headline)
        },
        modifier = modifier.padding(bottom = 27.dp),
        style = PretendardTextStyle.headline_bold
    )
}

@Preview
@Composable
fun JoinPreview(){
    JoinScreen()
}