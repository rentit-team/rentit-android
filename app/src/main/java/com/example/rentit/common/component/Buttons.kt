package com.example.rentit.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun FilterButton(title: String, modifier: Modifier = Modifier, iconContent: @Composable () -> Unit = {}) {
    OutlinedButton(
        modifier = modifier.height(30.dp),
        onClick = { },
        shape = RoundedCornerShape(20.dp),
        border = CommonBorders.basicBorder(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material.Text(
                text = title,
                style = MaterialTheme.typography.labelLarge)
            iconContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview(){
    RentItTheme {
        Column(){
            CommonButton(text = "Button", containerColor = PrimaryBlue500, contentColor = Color.White) {}
            FilterButton("Button") {}
        }
    }
}
