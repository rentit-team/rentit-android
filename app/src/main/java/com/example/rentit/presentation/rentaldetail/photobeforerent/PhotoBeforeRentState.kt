package com.example.rentit.presentation.rentaldetail.photobeforerent

import android.net.Uri

private const val MAX_PHOTO_CNT = 5
private const val MIN_PHOTO_CNT = 2

data class PhotoBeforeRentState(
    val takenPhotoUris: List<Uri> = emptyList(),
    val maxPhotoCnt: Int = MAX_PHOTO_CNT,
    val minPhotoCnt: Int = MIN_PHOTO_CNT,
    val isUploadInProgress: Boolean = false,
) {
    val isMaxPhotoTaken: Boolean
        get() = takenPhotoUris.size >= maxPhotoCnt
    val isRegisterEnabled: Boolean
        get() = takenPhotoUris.size >= minPhotoCnt && !isUploadInProgress
}