package com.example.rentit.common.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@Composable
fun CommonTagButton(text: String, onClick: () -> Unit) {
    var isSelected by remember { mutableStateOf(false) }
    OutlinedButton(
        onClick = { isSelected = !isSelected; onClick(); },
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(1.dp, if(isSelected) PrimaryBlue500 else Gray200),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            text = text,
            style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommonTagButton() {
    RentItTheme {
        CommonTagButton("버튼") {}
    }
}