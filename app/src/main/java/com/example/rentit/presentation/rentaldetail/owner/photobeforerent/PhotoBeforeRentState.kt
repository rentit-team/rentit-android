package com.example.rentit.presentation.rentaldetail.owner.photobeforerent

import android.net.Uri


data class PhotoBeforeRentState(
    val takenPhotoUris: List<Uri> = emptyList(),
    val maxPhotoCnt: Int = 6,
    val minPhotoCnt: Int = 2,
    val isUploadInProgress: Boolean = false,
) {
    val isMaxPhotoTaken: Boolean
        get() = takenPhotoUris.size >= maxPhotoCnt
    val isRegisterEnabled: Boolean
        get() = takenPhotoUris.size >= minPhotoCnt && !isUploadInProgress
}