package com.example.rentit.presentation.rentaldetail.rentalphotocheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.rental.repository.RentalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RentalPhotoCheckViewModel @Inject constructor(
    private val rentalRepository: RentalRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(RentalPhotoCheckState())
    val uiState: StateFlow<RentalPhotoCheckState> = _uiState

    private val _sideEffect = MutableSharedFlow<RentalPhotoCheckSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun fetchBeforePhotoUrls(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            rentalRepository.getRentalPhotos(productId, reservationId)
                .onSuccess {
                    val beforeUrls = it.rentalBefore.map { photo -> photo.url }
                    val afterUrls = it.returnBefore.map { photo -> photo.url }
                    handleFetchPhotoSuccess(beforeUrls, afterUrls)
                }.onFailure {
                    handleFetchPhotoFailed()
                }
        }
    }

    private fun handleFetchPhotoSuccess(beforeUrls: List<String>, afterUrls: List<String>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                photoBeforeRentUrls = beforeUrls,
                photoAfterRentUrls = afterUrls,
                previewPhotoUrl = beforeUrls.getOrNull(_uiState.value.currentPageIndex)
            )
        }
    }

    private fun handleFetchPhotoFailed() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showFailedPhotoLoadDialog = true)
        }
    }

    fun closeAndNavigateBack() {
        _uiState.value = _uiState.value.copy(showFailedPhotoLoadDialog = false)
        navigateBack()
    }

    fun navigateBack() {
        viewModelScope.launch {
            _sideEffect.emit(RentalPhotoCheckSideEffect.PopBackToRentalDetail)
        }
    }

    fun changePreviewPhotoUrl(url: String?) {
        _uiState.value = _uiState.value.copy(previewPhotoUrl = url)
    }

    fun goToNextPage() {
        if (_uiState.value.currentPageIndex < uiState.value.totalPhotoCnt - 1)
            _uiState.value =
                _uiState.value.copy(currentPageIndex = _uiState.value.currentPageIndex + 1)
    }

    fun goToPreviousPage() {
        if (_uiState.value.currentPageIndex > 0 )
            _uiState.value =
                _uiState.value.copy(currentPageIndex = _uiState.value.currentPageIndex - 1)
    }
}