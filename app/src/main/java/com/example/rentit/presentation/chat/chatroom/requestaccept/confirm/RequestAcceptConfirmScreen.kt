package com.example.rentit.presentation.chat.chatroom.requestaccept.confirm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestAcceptConfirmScreen(navHostController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .screenHorizontalPadding()
            .padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_check_circle),
            contentDescription = stringResource(
                id = R.string.content_description_for_ic_check
            ),
            tint = PrimaryBlue500
        )
        Text(
            modifier = Modifier.padding(vertical = 34.dp),
            text = stringResource(id = R.string.screen_accept_confirm_title),
            style = MaterialTheme.typography.headlineLarge
        )
        CommonButton(
            text = stringResource(id = R.string.screen_accept_confirm_btn_complete),
            containerColor = PrimaryBlue500,
            contentColor = Color.White,
        ) {
            navHostController.popBackStack()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    RentItTheme {
        RequestAcceptConfirmScreen(rememberNavController())
    }
}