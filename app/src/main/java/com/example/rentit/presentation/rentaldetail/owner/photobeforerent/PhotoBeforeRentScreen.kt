package com.example.rentit.presentation.rentaldetail.owner.photobeforerent

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.basicRoundedGrayBorder
import com.example.rentit.common.component.item.RemovableImageBox
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.White

@Composable
fun PhotoBeforeRentScreen(
    minPhotoCnt: Int,
    maxPhotoCnt: Int,
    isRegisterEnabled: Boolean,
    isMaxPhotoTaken: Boolean,
    takenPhotoUris: List<Uri>,
    onTakePhoto: () -> Unit,
    onRemovePhoto: (Uri) -> Unit,
    onRegister: () -> Unit,
) {
    Scaffold(
        topBar = { CommonTopAppBar {} },
        bottomBar = { CommonButton(
                modifier = Modifier.screenHorizontalPadding().padding(bottom = 30.dp),
                text = stringResource(R.string.screen_photo_before_rent_btn_registration),
                enabled = isRegisterEnabled,
                containerColor = if (isRegisterEnabled) PrimaryBlue500 else Gray200,
                contentColor = White,
                onClick = onRegister
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .screenHorizontalPadding()
        ) {

            PhotoBeforeRentGuide(minPhotoCnt, maxPhotoCnt)

            TakePhotoButton(
                maxPhotoCnt = maxPhotoCnt,
                isMaxPhotoTaken = isMaxPhotoTaken,
                onTakePhoto = onTakePhoto
            )

            TakenPhotos(takenPhotoUris, onRemovePhoto)
        }
    }
}

@Composable
fun PhotoBeforeRentGuide(minPhotoCnt: Int, maxPhotoCnt: Int) {
    Text(
        modifier = Modifier.padding(top = 16.dp, bottom = 14.dp),
        text = stringResource(R.string.screen_photo_before_rent_title),
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        modifier = Modifier.padding(bottom = 20.dp),
        text = stringResource(R.string.screen_photo_before_rent_guide, minPhotoCnt, maxPhotoCnt),
        style = MaterialTheme.typography.labelMedium.copy(lineHeight = 20.sp)
    )
}

@Composable
fun TakePhotoButton(maxPhotoCnt: Int, isMaxPhotoTaken: Boolean = false, onTakePhoto: () -> Unit) {
    val btnContentDesc = stringResource(R.string.screen_photo_before_rent_take_photo_btn_content_description)

    Box(
        modifier = Modifier
            .semantics { contentDescription = btnContentDesc }
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .clip(RoundedCornerShape(20.dp))
            .basicRoundedGrayBorder()
            .clickable(enabled = !isMaxPhotoTaken) { onTakePhoto() },
        contentAlignment = Alignment.Center
    ) {
        if(isMaxPhotoTaken) {
            androidx.compose.material.Text(
                stringResource(R.string.screen_photo_before_rent_take_photo_text_max_photo_taken, maxPhotoCnt),
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
fun TakenPhotos(photoList: List<Uri>, onRemoveClick: (Uri) -> Unit) {
    Row(
        Modifier
            .horizontalScroll(state = rememberScrollState())
            .padding(top = 20.dp)
    ) {
        photoList.forEach { uri ->
            RemovableImageBox(140.dp, 4f / 3f, uri) { onRemoveClick(it) }
            Spacer(Modifier.width(10.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PhotoBeforeRentScreenPreview() {
    RentItTheme {
        PhotoBeforeRentScreen(
            minPhotoCnt = 2,
            maxPhotoCnt = 6,
            isRegisterEnabled = true,
            isMaxPhotoTaken = true,
            takenPhotoUris = emptyList(),
            onTakePhoto = {},
            onRemovePhoto = {},
            onRegister = {},
        )
    }
}