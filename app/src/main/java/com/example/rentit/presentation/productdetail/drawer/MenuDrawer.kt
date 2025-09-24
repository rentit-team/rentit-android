package com.example.rentit.presentation.productdetail.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.rentItScreenHorizontalPadding
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.RentItTheme

@Composable
fun MenuDrawer(
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.22f)
            .rentItScreenHorizontalPadding(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TextButton(
            onClick = onEditClick
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_pencil),
                    contentDescription = null,
                    tint = AppBlack
                )
                Text(text = "글 수정하기", style = MaterialTheme.typography.bodyLarge)
            }
        }
        TextButton(
            onClick = onDeleteClick
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp).padding(2.dp),
                    painter = painterResource(R.drawable.ic_trash_bin),
                    contentDescription = null,
                    tint = AppBlack
                )
                Text(text = "삭제하기", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCategoryTagDrawer() {
    RentItTheme {
        MenuDrawer()
    }
}