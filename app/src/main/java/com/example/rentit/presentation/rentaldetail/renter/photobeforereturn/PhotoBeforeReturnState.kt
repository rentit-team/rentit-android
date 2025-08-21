package com.example.rentit.presentation.rentaldetail.renter.photobeforereturn

import android.net.Uri

data class PhotoBeforeReturnState(
    val currentPageIndex: Int = 0,
    val beforePhotoUrls: List<String> = emptyList(),
    val takenPhotoUris: List<Uri> = emptyList(),
    val isUploadInProgress: Boolean = false,
    val showFailedPhotoLoadDialog: Boolean = false,
) {
    val currentPageNumber
        get() = currentPageIndex + 1
    val requiredPhotoCnt
        get() = beforePhotoUrls.size
    val takenPhotoCnt
        get() = takenPhotoUris.count { it != Uri.EMPTY }
    val isLastPage: Boolean
        get() = currentPageIndex == beforePhotoUrls.size - 1
    val isNextAvailable: Boolean
        get() = (takenPhotoUris.getOrNull(currentPageIndex) ?: Uri.EMPTY) != Uri.EMPTY
    val isRegisterAvailable: Boolean
        get() = currentPageIndex == beforePhotoUrls.lastIndex &&
                (takenPhotoUris.getOrNull(currentPageIndex) ?: Uri.EMPTY) != Uri.EMPTY &&
                !isUploadInProgress
    val isBackAvailable: Boolean
        get() = currentPageIndex > 0
    val currentBeforePhotoUrl: String
        get() = beforePhotoUrls.getOrNull(currentPageIndex) ?: ""
    val takenPhotoUri: Uri
        get() = takenPhotoUris.getOrNull(currentPageIndex) ?: Uri.EMPTY
}