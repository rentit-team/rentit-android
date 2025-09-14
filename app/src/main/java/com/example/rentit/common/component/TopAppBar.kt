package com.example.rentit.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.RentItTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CommonTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    showMenu: Boolean = false,
    showDownLoadReceipt: Boolean = false,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onDownLoadReceiptClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White)
            .padding(top = 5.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = stringResource(id = R.string.common_top_app_bar_back_icon_description),
                tint = AppBlack
            )
        }
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyLarge,
            color = AppBlack
        )
        if(showMenu) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dots),
                    contentDescription = stringResource(id = R.string.common_top_app_bar_menu_icon_description),
                    tint = AppBlack
                )
            }
        }
        if(showDownLoadReceipt) {
            IconButton(
                onClick = onDownLoadReceiptClick,
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_receipt),
                    contentDescription = stringResource(id = R.string.common_top_app_bar_menu_icon_description),
                    tint = AppBlack
                )
            }
        }
    }
}

@Preview
@Composable
fun TopAppBarPreview() {
    RentItTheme {
        CommonTopAppBar(Modifier, "제목", false, true)
    }
}