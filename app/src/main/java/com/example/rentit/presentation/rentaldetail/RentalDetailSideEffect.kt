package com.example.rentit.presentation.rentaldetail

import java.io.File

sealed class RentalDetailSideEffect {
    data class DocumentLoadSuccess(val file: File) : RentalDetailSideEffect()
    data object NavigateBack: RentalDetailSideEffect()
    data object NavigateToProductDetail: RentalDetailSideEffect()
    data object NavigateToPay: RentalDetailSideEffect()
    data object NavigateToPhotoBeforeRent: RentalDetailSideEffect()
    data object NavigateToPhotoBeforeReturn: RentalDetailSideEffect()
    data object NavigateToRentalPhotoCheck: RentalDetailSideEffect()
    data class NavigateToChatRoom(val chatRoomId: String): RentalDetailSideEffect()
    data object ToastDocumentLoadFailed : RentalDetailSideEffect()
    data object ToastErrorGetCourierNames: RentalDetailSideEffect()
    data object ToastSuccessTrackingRegistration: RentalDetailSideEffect()
    data object ToastErrorTrackingRegistration: RentalDetailSideEffect()
    data object ToastCancelRentalSuccess: RentalDetailSideEffect()
    data object ToastCancelRentalFailed: RentalDetailSideEffect()
    data object ToastAcceptRentalSuccess: RentalDetailSideEffect()
    data object ToastAcceptRentalFailed: RentalDetailSideEffect()
    data object ToastAcceptedMessageSendSuccess: RentalDetailSideEffect()
    data object ToastChatRoomError: RentalDetailSideEffect()
    data class CommonError(val throwable: Throwable): RentalDetailSideEffect()
}