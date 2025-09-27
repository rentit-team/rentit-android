package com.example.rentit.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.core.exceptions.UnauthorizedException
import com.example.rentit.domain.user.usecase.CheckUserSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SplashViewModel"

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUserSessionUseCase: CheckUserSessionUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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
                        is UnauthorizedException -> {
                            navigateToLogin()
                            Log.w(TAG, "토큰 누락으로 사용자 정보 조회 실패", e)
                        }
                        else -> {
                            Log.e(TAG, "사용자 정보 조회 실패 (서버/네트워크)", e)
                            _splashSideEffect.send(SplashSideEffect.CommonError(e))
                        }
                    }
                }
            stopLoading()
        }
    }

    private fun startLoading() {
        _isLoading.value = true
    }

    private fun stopLoading() {
        _isLoading.value = false
    }

    fun retryCheckUserSession() {
        checkUserSession()
    }

    private suspend fun navigateToMain() {
        _splashSideEffect.send(SplashSideEffect.NavigateToMain)
    }

    private suspend fun navigateToLogin() {
        _splashSideEffect.send(SplashSideEffect.NavigateToLogin)
    }
}