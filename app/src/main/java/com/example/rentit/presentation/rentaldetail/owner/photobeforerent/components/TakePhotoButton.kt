package com.example.rentit.presentation.rentaldetail.owner.photobeforerent.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.basicRoundedGrayBorder
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.rentaldetail.components.rememberTakePhotoLauncher

private const val boxHeightFraction = 0.4f

@Composable
fun TakePhotoButton(isMaxPhotoTaken: Boolean = false, onTakePhotoSuccess: (Uri) -> Unit, onTakePhotoFail: () -> Unit) {
    val launchCameraWithNewPhotoUri = rememberTakePhotoLauncher(onTakePhotoSuccess, onTakePhotoFail)
    val btnContentDesc = stringResource(R.string.screen_photo_before_rent_take_photo_btn_content_description)

    Box(
        modifier = Modifier
            .semantics { contentDescription = btnContentDesc }
            .fillMaxWidth()
            .fillMaxHeight(boxHeightFraction)
            .clip(RoundedCornerShape(20.dp))
            .basicRoundedGrayBorder()
            .clickable(enabled = !isMaxPhotoTaken) { launchCameraWithNewPhotoUri() },
        contentAlignment = Alignment.Center
    ) {
        if(isMaxPhotoTaken) {
            Text(
                stringResource(R.string.screen_photo_before_rent_take_photo_text_max_photo_taken),
                color = Gray400
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = stringResource(R.string.content_description_for_ic_camera),
                tint = Gray300
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TakePhotoButtonPreview() {
    RentItTheme {
        TakePhotoButton(
            onTakePhotoSuccess = {},
            onTakePhotoFail = {}
        )
    }
}