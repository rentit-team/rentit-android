package com.example.rentit.presentation.home.createpost.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonBorders
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@Composable
fun TagButton(
    text: String,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isSelected by remember { mutableStateOf(isSelected) }
    OutlinedButton(
        onClick = {
            isSelected = !isSelected
            onClick()
        },
        shape = RoundedCornerShape(25.dp),
        border = CommonBorders.basicBorder(color = if(isSelected) PrimaryBlue500 else Gray200),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    modifier = Modifier
                        .height(12.dp)
                        .padding(end = 6.dp),
                    painter = painterResource(id = R.drawable.ic_check),
                    tint = PrimaryBlue500,
                    contentDescription = stringResource(id = R.string.common_tag_btn_icon_check_description)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) PrimaryBlue500 else AppBlack
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTagButton() {
    RentItTheme {
        TagButton("버튼", false) {}
    }
}