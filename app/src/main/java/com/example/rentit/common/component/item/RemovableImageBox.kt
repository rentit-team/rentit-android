package com.example.rentit.common.component.item

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.basicRoundedGrayBorder
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.RentItTheme
import androidx.core.net.toUri
import com.example.rentit.common.component.LoadableUriImage

@Composable
fun RemovableImageBox(width: Dp, aspectRatio: Float, imageUri: Uri, onImageRemoveClick: (Uri) -> Unit) {
    Box(modifier = Modifier
        .width(width)
        .aspectRatio(aspectRatio)
        .clip(RoundedCornerShape(20.dp))
        .basicRoundedGrayBorder()) {
        LoadableUriImage(
            modifier = Modifier.fillMaxWidth(),
            imgUri = imageUri,
            defaultDescResId = R.string.screen_product_create_selected_image_description
        )
        IconButton(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .size(20.dp)
                .background(Color(255, 255, 255, 160)),
            onClick = { onImageRemoveClick(imageUri) }
        ) {
            Icon(
                modifier = Modifier.size(8.dp),
                painter = painterResource(id = R.drawable.ic_x_bold),
                tint = AppBlack,
                contentDescription = stringResource(R.string.content_description_for_ic_x_delete)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun RemovableImageBoxPreview() {
    RentItTheme {
        RemovableImageBox(
            width = 160.dp,
            aspectRatio = 4f/3f,
            imageUri = "".toUri(),
            onImageRemoveClick = {}
        )
    }
}
