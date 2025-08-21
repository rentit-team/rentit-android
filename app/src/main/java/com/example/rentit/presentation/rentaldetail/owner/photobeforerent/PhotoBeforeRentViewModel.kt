package com.example.rentit.presentation.rentaldetail.owner.photobeforerent

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.PhotoRegistrationType
import com.example.rentit.data.rental.repository.RentalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PhotoBeforeRentViewModel @Inject constructor(
    private val rentalRepository: RentalRepository
): ViewModel() {

    private val _sideEffect = MutableSharedFlow<PhotoBeforeRentSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val _uiState = MutableStateFlow(PhotoBeforeRentState())
    val uiState: StateFlow<PhotoBeforeRentState> = _uiState

    private val takePhotoFiles = mutableListOf<File>()

    fun onTakePhotoSuccess(uri: Uri, file: File) {
        _uiState.value = _uiState.value.copy(takenPhotoUris = _uiState.value.takenPhotoUris + uri)
        takePhotoFiles.add(file)
    }

    fun onRemovePhotoSuccess(uri: Uri) {
        _uiState.value = _uiState.value.copy(takenPhotoUris = _uiState.value.takenPhotoUris - uri)
    }

    fun uploadPhotos(productId: Int, reservationId: Int, ) {
        val files = takePhotoFiles.map { fileToMultipart(it) }
        viewModelScope.launch {
            rentalRepository.postPhotoRegistration(productId, reservationId, PhotoRegistrationType.RENTAL_BEFORE, files)
                .onSuccess {
                    handlePhotoUploadSuccess()
                }.onFailure {
                    toastPhotoUploadFailed()
                }
        }
    }

    private fun fileToMultipart(file: File): MultipartBody.Part {
        val requestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("images", file.name, requestBody)
    }

    private fun handlePhotoUploadSuccess() {
        viewModelScope.launch {
            _sideEffect.emit(PhotoBeforeRentSideEffect.PopBackToRentalDetail)
            _sideEffect.emit(PhotoBeforeRentSideEffect.ToastUploadSuccess)
        }
    }

    private fun toastPhotoUploadFailed() {
        viewModelScope.launch {
            _sideEffect.emit(PhotoBeforeRentSideEffect.ToastUploadFailed)
        }
    }
}