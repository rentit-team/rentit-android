package com.example.rentit.presentation.main

import androidx.lifecycle.ViewModel
import com.example.rentit.core.error.UnauthorizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState:StateFlow<MainState> = _uiState

    private var retryAction: () -> Unit = {}

    private fun updateState(transformation: MainState.() -> MainState) {
        val currentState = _uiState.value
        _uiState.value = currentState.transformation()
    }

    fun setRetryAction(onRetry: () -> Unit) {
        retryAction = onRetry
    }

    fun handleError(e: Throwable) {
        when(e) {
            is UnauthorizedException -> {
                // TODO: 로그아웃 및 로그인 화면으로 이동
            }
            is IOException -> {
                updateState { copy(showNetworkErrorDialog = true) }
            }
            else -> {
                updateState { copy(showServerErrorDialog = true) }
            }
        }
    }

    fun retry() {
        updateState {
            copy(
                showNetworkErrorDialog = false,
                showServerErrorDialog = false
            )
        }
        retryAction.invoke()
    }
}