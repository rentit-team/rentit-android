package com.example.rentit.common.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.RentItTheme

@Composable
fun CommonLabelButton(onClick: () -> Unit, text: String) {
    OutlinedButton(
        modifier = Modifier.height(26.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Gray200),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = text,
            style = MaterialTheme.typography.labelMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommonLabelButton() {
    RentItTheme {
        CommonLabelButton({}, "버튼")
    }
}