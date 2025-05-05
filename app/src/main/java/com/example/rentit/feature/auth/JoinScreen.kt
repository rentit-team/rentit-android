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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonTextField
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.moveScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.PretendardTextStyle
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@Composable
fun JoinScreen(navHostController: NavHostController) {
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
            HighlightedHeadline(Modifier.align(Alignment.Start))
            CommonTextField(onValueChange = {}, placeholder = stringResource(R.string.app_name))
            Spacer(modifier = Modifier.weight(1f))
            CommonButton(stringResource(R.string.screen_join_btn_text)) {
                moveScreen(navHostController, NavigationRoutes.MAIN, isInclusive = true)
            }
        }
    }
}


@Composable
fun HighlightedHeadline(modifier: Modifier) {
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

@Preview
@Composable
fun JoinPreview(){
    RentItTheme {
        JoinScreen(rememberNavController())
    }
}