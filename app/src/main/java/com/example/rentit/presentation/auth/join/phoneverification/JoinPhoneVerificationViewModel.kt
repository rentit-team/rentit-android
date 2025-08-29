package com.example.rentit.presentation.auth.join.phoneverification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.rental.exception.TooManyRequestException
import com.example.rentit.domain.rental.exception.VerificationFailedException
import com.example.rentit.domain.rental.exception.VerificationRequestNotFoundException
import com.example.rentit.domain.user.repository.UserRepository
import com.example.rentit.presentation.auth.join.phoneverification.model.VerificationError
import com.example.rentit.presentation.auth.join.phoneverification.model.toUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class JoinPhoneVerificationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(JoinPhoneVerificationState())
    val state: StateFlow<JoinPhoneVerificationState> = _state

    private val _sideEffect = MutableSharedFlow<JoinPhoneVerificationSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun requestCode() {
        clearErrorMessage()
        val phoneNumber = _state.value.phoneNumber

        viewModelScope.launch {
            userRepository.sendPhoneCode(phoneNumber)
                .onSuccess {
                    startRemainingTimeCountdown(it.expiresIn)
                }
                .onFailure { e ->
                    when(e) {
                        is TooManyRequestException -> _sideEffect.emit(JoinPhoneVerificationSideEffect.ToastTooManyRequests)
                        else -> _sideEffect.emit(JoinPhoneVerificationSideEffect.ToastSendCodeFailed)
                    }
                }
        }
    }

    private fun startRemainingTimeCountdown(expireTime: Int) {
        viewModelScope.launch {
            repeat(expireTime){
                _state.value = _state.value.copy(remainingTime = expireTime - it)
                delay(1000)
            }
        }
    }

    fun codeConfirm() {
        clearErrorMessage()
        val phoneNumber = _state.value.phoneNumber
        val code = _state.value.code

        viewModelScope.launch {
            userRepository.verifyPhoneCode(phoneNumber, code)
                .onSuccess {
                    _sideEffect.emit(JoinPhoneVerificationSideEffect.ToastVerificationSuccess)
                    navigateToNickname()
                }
                .onFailure { e ->
                        println(e.message)
                    when (e) {
                        // 서버 호출은 성공했지만, 인증은 실패한 경우
                        is VerificationFailedException -> {
                            updateErrorMessage(VerificationError.from(e.message))
                        }
                        // 인증 요청 자체가 없는 경우 (사용자가 인증번호 요청을 하지 않은 상태)
                        is VerificationRequestNotFoundException -> {
                            updateErrorMessage(VerificationError.Failed)
                        }
                        // 서버 문제나 네트워크 오류 등 시스템적 실패
                        else -> {
                            _sideEffect.emit(JoinPhoneVerificationSideEffect.ToastVerificationFailed)
                        }
                    }
                }
        }
    }

    private fun updateErrorMessage(errorType: VerificationError) {
        _state.value = _state.value.copy(errorMessageRes = errorType.toUiMessage())
    }

    private fun clearErrorMessage() {
        _state.value = _state.value.copy(errorMessageRes = null)
    }

    fun changePhoneNumber(phoneNumber: String) {
        _state.value = _state.value.copy(phoneNumber = phoneNumber)
    }

    fun changeCode(code: String) {
        clearErrorMessage()
        _state.value = _state.value.copy(code = code)
    }

    fun navigateBack() {
        viewModelScope.launch {
            _sideEffect.emit(JoinPhoneVerificationSideEffect.NavigateBack)
        }
    }

    private fun navigateToNickname() {
        viewModelScope.launch {
            _sideEffect.emit(JoinPhoneVerificationSideEffect.NavigateToNickname)
        }
    }
}