package com.example.rentit.presentation.rentaldetail.rentalphotocheck

data class RentalPhotoCheckState(
    val currentPageIndex: Int = 0,
    val photoBeforeRentUrls: List<String> = emptyList(),
    val photoAfterRentUrls: List<String> = emptyList(),
    val previewPhotoUrl: String? = null,
    val showFailedPhotoLoadDialog: Boolean = false
) {
    val currentPageNumber
        get() = currentPageIndex + 1
    val beforePhotoUrl
        get() = photoBeforeRentUrls.getOrNull(currentPageIndex)
    val afterPhotoUrl
        get() = photoAfterRentUrls.getOrNull(currentPageIndex)
    val totalPhotoCnt
        get() = minOf(photoBeforeRentUrls.size, photoAfterRentUrls.size)
    val isNextAvailable
        get() = currentPageIndex < totalPhotoCnt - 1
    val isBackAvailable
        get() = currentPageIndex > 0
}