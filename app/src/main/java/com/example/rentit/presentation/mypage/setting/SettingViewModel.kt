package com.example.rentit.presentation.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.auth.respository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SettingState())
    val uiState: StateFlow<SettingState> = _uiState

    private val _sideEffect = MutableSharedFlow<SettingSideEffect>()
    val sideEffect: SharedFlow<SettingSideEffect> = _sideEffect

    private fun updateState(transform: SettingState.() -> SettingState) {
        _uiState.value = _uiState.value.transform()
    }

    private fun emitSideEffect(sideEffect: SettingSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    fun showLogoutConfirmDialog() {
        updateState { copy(showLogoutConfirmDialog = true) }
    }

    fun hideLogoutConfirmDialog() {
        updateState { copy(showLogoutConfirmDialog = false) }
    }

    fun onLogoutConfirmed() {
        updateState { copy(showLogoutConfirmDialog = false) }
        authRepository.clearPrefs()
        emitSideEffect(SettingSideEffect.ToastLogoutComplete)
        emitSideEffect(SettingSideEffect.NavigateToLogin)
    }
}