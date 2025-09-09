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

    private fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    suspend fun fetchPhotoUrls(productId: Int, reservationId: Int) {
        setLoading(true)
        rentalRepository.getRentalPhotos(productId, reservationId)
            .onSuccess {
                val beforeUrls = it.rentalBefore.map { photo -> photo.url }
                val afterUrls = it.returnBefore.map { photo -> photo.url }
                handleFetchPhotoSuccess(beforeUrls, afterUrls)
            }.onFailure { e ->
                _sideEffect.emit(RentalPhotoCheckSideEffect.CommonError(e))
            }
        setLoading(false)
    }

    private fun handleFetchPhotoSuccess(beforeUrls: List<String>, afterUrls: List<String>) {
        _uiState.value = _uiState.value.copy(
            photoBeforeRentUrls = beforeUrls,
            photoAfterRentUrls = afterUrls,
            previewPhotoUrl = beforeUrls.getOrNull(_uiState.value.currentPageIndex)
        )
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

    fun retryFetchPhotoUrls(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            fetchPhotoUrls(productId, reservationId)
        }
    }
}