package com.example.rentit.presentation.pay

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
): ViewModel() {

    private val _isDialogVisible = MutableStateFlow(false)
    val isDialogVisible: StateFlow<Boolean> = _isDialogVisible

    fun setDialogVisibility(isVisible: Boolean) {
        _isDialogVisible.value = isVisible
    }
}