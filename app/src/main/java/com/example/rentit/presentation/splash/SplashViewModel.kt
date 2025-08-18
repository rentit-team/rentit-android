package com.example.rentit.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _splashSideEffect = Channel<SplashSideEffect>()
    val splashSideEffect = _splashSideEffect.receiveAsFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            val token = userRepository.getTokenFromPrefs()
            if(token.isNullOrEmpty()){
                navigateToLogin()
                return@launch
            }

            val localUserId = userRepository.getAuthUserIdFromPrefs()
            if(localUserId == -1L){
                userRepository.getMyInfo().onSuccess {
                    userRepository.saveAuthUserIdToPrefs(it.data.userId)
                }.onFailure {
                    // 서버가 토큰 에러를 구분하지 않음 -> 500 포함 모든 에러 재로그인 유도
                    navigateToLogin()
                    return@launch
                }
            }
            _splashSideEffect.send(SplashSideEffect.NavigateToMain)
        }
    }

    private suspend fun navigateToLogin() {
        userRepository.clearPrefs()
        _splashSideEffect.send(SplashSideEffect.NavigateToLogin)
    }
}