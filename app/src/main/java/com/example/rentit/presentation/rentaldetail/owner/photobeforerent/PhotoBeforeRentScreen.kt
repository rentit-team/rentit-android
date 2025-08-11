package com.example.rentit.presentation.rentaldetail.owner.photobeforerent

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.item.RemovableImageBox
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.White
import com.example.rentit.presentation.rentaldetail.owner.photobeforerent.components.TakePhotoButton

@Composable
fun PhotoBeforeRentScreen(
    minPhotoCnt: Int,
    maxPhotoCnt: Int,
    isRegisterEnabled: Boolean,
    isMaxPhotoTaken: Boolean,
    takenPhotoUris: List<Uri>,
    onTakePhotoSuccess: (Uri) -> Unit,
    onRemovePhoto: (Uri) -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { CommonTopAppBar {} },
        bottomBar = { CommonButton(
                modifier = Modifier.screenHorizontalPadding().padding(bottom = 30.dp),
                text = stringResource(R.string.screen_photo_before_rent_btn_registration),
                enabled = isRegisterEnabled,
                containerColor = if (isRegisterEnabled) PrimaryBlue500 else Gray200,
                contentColor = White
            ) { }
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .screenHorizontalPadding()
        ) {

            PhotoBeforeRentGuide(minPhotoCnt, maxPhotoCnt)

            TakePhotoButton(
                isMaxPhotoTaken = isMaxPhotoTaken,
                onTakePhotoSuccess = onTakePhotoSuccess,
                onTakePhotoFail = { Toast.makeText(context, context.getString(R.string.toast_take_photo_fail), Toast.LENGTH_SHORT).show() }
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
    var takenPhotoUris = listOf<Uri>()
    RentItTheme {
        PhotoBeforeRentScreen(
            minPhotoCnt = 2,
            maxPhotoCnt = 6,
            isRegisterEnabled = true,
            isMaxPhotoTaken = true,
            takenPhotoUris = takenPhotoUris,
            onTakePhotoSuccess = { uri -> takenPhotoUris = listOf(uri) + takenPhotoUris },
            onRemovePhoto = { uri -> takenPhotoUris -= uri },
        )
    }
}