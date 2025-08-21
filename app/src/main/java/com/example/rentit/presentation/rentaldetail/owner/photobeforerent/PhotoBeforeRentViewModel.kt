package com.example.rentit.presentation.rentaldetail.owner.photobeforerent

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.rentit.data.rental.repository.RentalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PhotoBeforeRentViewModel @Inject constructor(
    private val rentalRepository: RentalRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(PhotoBeforeRentState())
    val uiState: StateFlow<PhotoBeforeRentState> = _uiState

    fun onTakePhotoSuccess(uri: Uri) {
        _uiState.value = _uiState.value.copy(takenPhotoUris = _uiState.value.takenPhotoUris + uri)
    }

    fun onRemovePhotoSuccess(uri: Uri) {
        _uiState.value = _uiState.value.copy(takenPhotoUris = _uiState.value.takenPhotoUris - uri)
    }
}