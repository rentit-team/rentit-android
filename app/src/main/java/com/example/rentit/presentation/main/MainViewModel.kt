package com.example.rentit.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.core.exceptions.UnauthorizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState:StateFlow<MainState> = _uiState

    private val _mainSideEffect = MutableSharedFlow<MainSideEffect>()
    val mainSideEffect: SharedFlow<MainSideEffect> = _mainSideEffect

    private var retryAction: () -> Unit = {}

    private fun updateState(transform: MainState.() -> MainState) {
        _uiState.update(transform)
    }

    private fun emitSideEffect(sideEffect: MainSideEffect) {
        viewModelScope.launch {
            _mainSideEffect.emit(sideEffect)
        }
    }

    fun setRetryAction(onRetry: () -> Unit) {
        retryAction = onRetry
    }

    fun handleError(e: Throwable) {
        when(e) {
            is UnauthorizedException -> {
                updateState { copy(showSessionExpiredDialog = true) }
            }
            is IOException -> {
                updateState { copy(showNetworkErrorDialog = true) }
            }
            else -> {
                updateState { copy(showServerErrorDialog = true) }
            }
        }
    }

    fun onSessionExpiredDialogConfirm() {
        emitSideEffect(MainSideEffect.NavigateToLogin)
        updateState { copy(showSessionExpiredDialog = false) }
    }

    fun retry() {
        updateState {
            copy(
                showNetworkErrorDialog = false,
                showServerErrorDialog = false
            )
        }
        retryAction()
    }
}