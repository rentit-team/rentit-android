package com.example.rentit.common.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.PrimaryBlue300
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@Composable
fun CommonButton(modifier: Modifier = Modifier, text: String, enabled: Boolean = true, containerColor: Color, contentColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    title: String,
    contentDesc: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    val borderColor = if(isSelected) PrimaryBlue500 else Gray200
    val textColor = if(isSelected) PrimaryBlue500 else AppBlack

    OutlinedButton(
        modifier = modifier.height(30.dp).semantics { contentDescription = contentDesc },
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        border = CommonBorders.basicBorder(borderColor),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = textColor
            )
        }
    }
}

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
    color: Color = AppBlack,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier.height(buttonHeight).semantics { contentDescription = text },
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = buttonHorizontalPadding),
        colors = ButtonDefaults.textButtonColors(contentColor = color)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = color
        )
        Spacer(Modifier.padding(textIconSpacing))
        Icon(
            modifier = Modifier.size(iconSize),
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = color
        )
    }
}

@Composable
fun ExtendedFAB(modifier: Modifier = Modifier, @DrawableRes iconRes: Int, @StringRes textRes: Int, onClick: () -> Unit) {
    val text = stringResource(textRes)
    ExtendedFloatingActionButton(
        modifier = modifier.semantics { contentDescription = text },
        containerColor = PrimaryBlue300,
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.padding(end = 12.dp).size(20.dp),
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = AppBlack
        )
        androidx.compose.material.Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ButtonPreview(){
    RentItTheme {
        Column(){
            CommonButton(text = "Button", containerColor = PrimaryBlue500, contentColor = Color.White) {}
            FilterButton(title = "Button") {}
            ArrowedTextButton(text = "Button") {}
            ExtendedFAB(iconRes = R.drawable.ic_camera, textRes = com.google.android.gms.base.R.string.common_signin_button_text) {}
        }
    }
}
