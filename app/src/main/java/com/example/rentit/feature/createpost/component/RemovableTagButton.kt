package com.example.rentit.feature.createpost.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.basicRoundedGrayBorder
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@Composable
fun RemovableTagButton(text: String, modifier: Modifier = Modifier, onRemoveClick: () -> Unit) {
    Row(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(25.dp))
            .basicRoundedGrayBorder(color = PrimaryBlue500)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 6.dp),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = PrimaryBlue500
        )
        IconButton(modifier = Modifier.width(12.dp), onClick = onRemoveClick) {
            Icon(
                modifier = Modifier.width(10.dp),
                painter = painterResource(id = R.drawable.ic_x),
                contentDescription = stringResource(id = R.string.common_removable_tag_btn_icon_delete_description),
                tint = PrimaryBlue500
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRemovableTagButton() {
    RentItTheme {
        RemovableTagButton("버튼") {}
    }
}