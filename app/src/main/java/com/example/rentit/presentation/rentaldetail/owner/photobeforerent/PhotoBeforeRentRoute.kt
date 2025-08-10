package com.example.rentit.presentation.rentaldetail.owner.photobeforerent

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


private const val minPhotoCnt = 2
private const val maxPhotoCnt = 6

@Composable
fun PhotoBeforeRentRoute() {
    var takenPhotoUris by remember { mutableStateOf(listOf<Uri>()) }
    val isMaxPhotoTaken = takenPhotoUris.size >= maxPhotoCnt
    val isRegisterEnabled = takenPhotoUris.size >= minPhotoCnt

    PhotoBeforeRentScreen(
        minPhotoCnt = minPhotoCnt,
        maxPhotoCnt = maxPhotoCnt,
        isRegisterEnabled = isRegisterEnabled,
        isMaxPhotoTaken = isMaxPhotoTaken,
        takenPhotoUris = takenPhotoUris,
        onTakePhotoSuccess = { uri -> takenPhotoUris = listOf(uri) + takenPhotoUris },
        onRemovePhoto = { uri -> takenPhotoUris -= uri },
    )
}