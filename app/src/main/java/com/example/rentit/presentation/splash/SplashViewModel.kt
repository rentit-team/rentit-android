package com.example.rentit.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.exception.MissingTokenException
import com.example.rentit.data.user.usecase.CheckUserSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "Splash"

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUserSessionUseCase: CheckUserSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashState())
    val uiState: StateFlow<SplashState> = _uiState

    private val _splashSideEffect = Channel<SplashSideEffect>()
    val splashSideEffect = _splashSideEffect.receiveAsFlow()

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            startLoading()
            checkUserSessionUseCase.invoke()
                .onSuccess { userId ->
                    navigateToMain()
                    Log.i(TAG, "사용자 정보 조회 성공: User ID $userId")
                }
                .onFailure { e ->
                    when(e) {
                        is MissingTokenException -> {
                            navigateToLogin()
                            Log.w(TAG, "토큰 누락으로 사용자 정보 조회 실패", e)
                        }
                        else -> {
                            showErrorDialog()
                            Log.e(TAG, "사용자 정보 조회 실패 (서버/네트워크)", e)
                        }
                    }
                }
            stopLoading()
        }
    }

    private fun startLoading() {
        _uiState.value = _uiState.value.copy(isLoading = true)
    }

    private fun stopLoading() {
        _uiState.value = _uiState.value.copy(isLoading = false)
    }

    private fun showErrorDialog() {
        _uiState.value = _uiState.value.copy(showErrorDialog = true)
    }

    fun retryCheckUserSession() {
        _uiState.value = _uiState.value.copy(showErrorDialog = false)
        checkUserSession()
    }

    private suspend fun navigateToMain() {
        _splashSideEffect.send(SplashSideEffect.NavigateToMain)
    }

    private suspend fun navigateToLogin() {
        _splashSideEffect.send(SplashSideEffect.NavigateToLogin)
    }
}