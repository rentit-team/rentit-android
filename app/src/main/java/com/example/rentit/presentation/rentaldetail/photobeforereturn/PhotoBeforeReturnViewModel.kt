package com.example.rentit.presentation.rentaldetail.photobeforereturn

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.PhotoRegistrationType
import com.example.rentit.domain.rental.repository.RentalRepository
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
class PhotoBeforeReturnViewModel @Inject constructor(
    private val rentalRepository: RentalRepository
): ViewModel() {
    private val _sideEffect = MutableSharedFlow<PhotoBeforeReturnSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val _uiState = MutableStateFlow(PhotoBeforeReturnState())
    val uiState: StateFlow<PhotoBeforeReturnState> = _uiState

    private var takePhotoFiles = mutableListOf<File>()

    private fun emitSideEffect(sideEffect: PhotoBeforeReturnSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    suspend fun fetchBeforePhotoUrls(productId: Int, reservationId: Int) {
        setLoading(true)
        rentalRepository.getRentalPhotos(productId, reservationId)
            .onSuccess {
                val beforeUrls = it.rentalBefore.map { photo -> photo.url }
                handleFetchPhotoSuccess(beforeUrls)
            }.onFailure { e ->
                emitSideEffect(PhotoBeforeReturnSideEffect.CommonError(e))
            }
        setLoading(false)
    }

    private fun handleFetchPhotoSuccess(beforeUrls: List<String>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(beforePhotoUrls = beforeUrls, takenPhotoUris = List(beforeUrls.size) { Uri.EMPTY })
            _uiState.value = _uiState.value.copy(beforePhotoUrls = beforeUrls, takenPhotoUris = List(beforeUrls.size) { Uri.EMPTY })
            takePhotoFiles = MutableList(beforeUrls.size) { File("") }
        }
    }

    fun goToNextPage() {
        if (_uiState.value.currentPageIndex < uiState.value.beforePhotoUrls.lastIndex)
            _uiState.value =
                _uiState.value.copy(currentPageIndex = _uiState.value.currentPageIndex + 1)
    }

    fun goToPreviousPage() {
        if (_uiState.value.currentPageIndex > 0 )
            _uiState.value =
                _uiState.value.copy(currentPageIndex = _uiState.value.currentPageIndex - 1)
    }

    fun onBeforeImageClicked() {
        _uiState.value = _uiState.value.copy(showFullImageDialog = true)
    }

    fun onFullImageDialogDismiss() {
        _uiState.value = _uiState.value.copy(showFullImageDialog = false)
    }

    fun onTakePhotoSuccess(uri: Uri, file: File) {
        val index = _uiState.value.currentPageIndex
        updatePhotoAtIndex(index, uri)
        takePhotoFiles[index] = file
    }

    private fun updatePhotoAtIndex(index: Int, uri: Uri) {
        _uiState.value = _uiState.value.copy(takenPhotoUris = _uiState.value.takenPhotoUris.toMutableList().apply { set(index, uri) })
    }

    fun uploadPhotos(productId: Int, reservationId: Int) {
        val files = takePhotoFiles.map { fileToMultipart(it) }
        viewModelScope.launch {
            setUploading(true)
            rentalRepository.postPhotoRegistration(productId, reservationId, PhotoRegistrationType.RETURN_BEFORE, files)
                .onSuccess {
                    handlePhotoUploadSuccess()
                }.onFailure {
                    toastPhotoUploadFailed()
                }
            setUploading(false)
        }
    }

    private fun fileToMultipart(file: File): MultipartBody.Part {
        val requestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("images", file.name, requestBody)
    }

    private fun handlePhotoUploadSuccess() {
        viewModelScope.launch {
            emitSideEffect(PhotoBeforeReturnSideEffect.PopBackToRentalDetail)
            emitSideEffect(PhotoBeforeReturnSideEffect.ToastUploadSuccess)
        }
    }

    private fun toastPhotoUploadFailed() {
        viewModelScope.launch {
            emitSideEffect(PhotoBeforeReturnSideEffect.ToastUploadFailed)
        }
    }

    private fun setUploading(isUploading: Boolean) {
        _uiState.value = _uiState.value.copy(isUploadInProgress = isUploading)
    }

    fun retryFetchBeforePhotoUrls(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            fetchBeforePhotoUrls(productId, reservationId)
        }
    }
}