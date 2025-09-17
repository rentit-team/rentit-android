package com.example.rentit.presentation.auth.join.nickname

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "JoinNicknameViewModel"

@HiltViewModel
class JoinNicknameViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinNicknameState())
    val uiState: StateFlow<JoinNicknameState> = _uiState

    private val _sideEffect = MutableSharedFlow<JoinNicknameSideEffect>()
    val sideEffect: SharedFlow<JoinNicknameSideEffect> = _sideEffect

    private fun emitSideEffect(sideEffect: JoinNicknameSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    private fun updateState(transform: JoinNicknameState.() -> JoinNicknameState) {
        _uiState.value = _uiState.value.transform()
    }

    private fun setIsLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }

    private fun setButtonEnabled(buttonEnabled: Boolean) {
        updateState { copy(buttonEnabled = buttonEnabled) }
    }

    fun onSignUp(name: String, email: String) {
        val nickname = _uiState.value.nickname
        val profileImageUrl = ""

        if(nickname.isBlank()) {
            updateState { copy(showNicknameBlankError = true) }
            return
        }

        viewModelScope.launch {
            setIsLoading(true)
            setButtonEnabled(false)
            repository.signUp(name, email, nickname, profileImageUrl)
                .onSuccess {
                    Log.i(TAG, "회원가입 - 닉네임 설정 성공")
                    emitSideEffect(JoinNicknameSideEffect.JoinNicknameSuccess)
                    emitSideEffect(JoinNicknameSideEffect.NavigateToHome)
                }.onFailure { e ->
                    Log.e(TAG, "회원가입 - 닉네임 설정 실패", e)
                    emitSideEffect(JoinNicknameSideEffect.JoinNicknameError)
                }
            setIsLoading(false)
            setButtonEnabled(true)
        }
    }

    fun updateNickname(nickname: String) {
        updateState { copy(nickname = nickname, showNicknameBlankError = false) }
    }
}