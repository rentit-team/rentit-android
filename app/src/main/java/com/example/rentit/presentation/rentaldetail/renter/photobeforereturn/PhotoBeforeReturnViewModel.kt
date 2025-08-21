package com.example.rentit.presentation.rentaldetail.renter.photobeforereturn

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
class PhotoBeforeReturnViewModel @Inject constructor(
    private val rentalRepository: RentalRepository
): ViewModel() {
    private val _sideEffect = MutableSharedFlow<PhotoBeforeReturnSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val _uiState = MutableStateFlow(PhotoBeforeReturnState())
    val uiState: StateFlow<PhotoBeforeReturnState> = _uiState

    private var takePhotoFiles = mutableListOf<File>()

    fun fetchBeforePhotoUrls() {
        /* 서버에서 이전 사진 가져오기 */
        val temp = listOf(
            "https://media.istockphoto.com/id/520700958/ko/%EC%82%AC%EC%A7%84/%EC%95%84%EB%A6%84%EB%8B%A4%EC%9A%B4-%EA%BD%83-%EB%B0%B0%EA%B2%BD%EA%B8%B0%EC%88%A0.jpg?s=1024x1024&w=is&k=20&c=Ci4unh6xc-hSxWAdqbY2CDGG4_8j8HnG1mPh4gbWYHs=",
            "https://media.istockphoto.com/id/916766854/ko/%EC%82%AC%EC%A7%84/%ED%95%98%EB%8A%98%EA%B3%BC-%EB%AC%B4%EC%A7%80%EA%B0%9C-%EB%B0%B0%EA%B2%BD.jpg?s=1024x1024&w=is&k=20&c=NCkA_Kd89y--eZb51AXzjTSreKPFCmACwm3scBJ9MXs=",
            "https://media.istockphoto.com/id/1208790371/ko/%EC%82%AC%EC%A7%84/%EA%B1%B4%EA%B0%95-%ED%95%9C-%EC%8B%A0%EC%84%A0%ED%95%9C-%EB%AC%B4%EC%A7%80%EA%B0%9C-%EC%83%89%EA%B9%94%EC%9D%98-%EA%B3%BC%EC%9D%BC%EA%B3%BC-%EC%95%BC%EC%B1%84-%EB%B0%B0%EA%B2%BD.jpg?s=1024x1024&w=is&k=20&c=YXOb4qT32YzXhh3iwJGo7IMtBWALt33f9DmhuyWHR70=",
        )
        _uiState.value = _uiState.value.copy(beforePhotoUrls = temp, takenPhotoUris = List(temp.size) { Uri.EMPTY })
        takePhotoFiles = MutableList(temp.size) { File("") }
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

    fun onTakePhotoSuccess(uri: Uri, file: File) {
        val index = _uiState.value.currentPageIndex
        updatePhotoAtIndex(index, uri)
        takePhotoFiles[index] = file
    }

    private fun updatePhotoAtIndex(index: Int, uri: Uri) {
        _uiState.value = _uiState.value.copy(takenPhotoUris = _uiState.value.takenPhotoUris.toMutableList().apply { set(index, uri) })
    }

    fun uploadPhotos(productId: Int, reservationId: Int, ) {
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
            _sideEffect.emit(PhotoBeforeReturnSideEffect.PopBackToRentalDetail)
            _sideEffect.emit(PhotoBeforeReturnSideEffect.ToastUploadSuccess)
        }
    }

    private fun toastPhotoUploadFailed() {
        viewModelScope.launch {
            _sideEffect.emit(PhotoBeforeReturnSideEffect.ToastUploadFailed)
        }
    }

    private fun setUploading(isUploading: Boolean) {
        _uiState.value = _uiState.value.copy(isUploadInProgress = isUploading)
    }
}