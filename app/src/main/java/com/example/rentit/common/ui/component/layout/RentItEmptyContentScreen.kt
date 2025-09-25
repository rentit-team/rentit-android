package com.example.rentit.common.ui.component.layout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme

private const val imageWidthFraction = 0.4f
private val textTopPadding = 16.dp

@Composable
fun RentItEmptyContentScreen(
    modifier: Modifier = Modifier,
    text: String = ""
) {
    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.weight(1f))
        Image(
            modifier = Modifier.fillMaxWidth(imageWidthFraction),
            painter = painterResource(id = R.drawable.img_empty_box),
            contentDescription = stringResource(id = R.string.content_description_for_img_empty_box)
        )
        Text(
            modifier = Modifier.padding(top = textTopPadding),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Gray400,
        )
        Spacer(Modifier.weight(2f))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun EmptyContentScreenPreview() {
    RentItTheme {
        RentItEmptyContentScreen()
    }
}