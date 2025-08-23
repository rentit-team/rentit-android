package com.example.rentit.presentation.auth.join.phoneverification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class JoinPhoneVerificationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(JoinPhoneVerificationState())
    val state: StateFlow<JoinPhoneVerificationState> = _state

    private val _sideEffect = MutableSharedFlow<JoinPhoneVerificationSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun changePhoneNumber(phoneNumber: String) {
        _state.value = _state.value.copy(phoneNumber = phoneNumber)
    }

    fun changeCode(code: String) {
        _state.value = _state.value.copy(code = code, showCodeError = false)
    }
}