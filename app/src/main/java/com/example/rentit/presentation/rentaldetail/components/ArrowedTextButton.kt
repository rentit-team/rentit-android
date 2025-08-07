package com.example.rentit.presentation.rentaldetail.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.RentItTheme

/**
 * 텍스트와 오른쪽 화살표 (>) 아이콘이 포함된 버튼 컴포넌트
 */

private val buttonHeight = 30.dp
private val buttonHorizontalPadding = 20.dp
private val textIconSpacing = 4.dp
private val iconSize = 10.dp

@Composable
fun ArrowedTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier.height(buttonHeight),
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = buttonHorizontalPadding),
        colors = ButtonDefaults.textButtonColors(contentColor = AppBlack)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.padding(textIconSpacing))
        Icon(modifier = Modifier.size(iconSize), painter = painterResource(R.drawable.ic_chevron_right), contentDescription = null)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    RentItTheme {
        ArrowedTextButton(
            text = "요청 응답하기"
        ) {}
    }
}