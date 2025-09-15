package com.example.rentit.presentation.auth.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.core.error.ConflictException
import com.example.rentit.domain.user.usecase.LoginAndInitializeAuthUseCase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginAndInitializeAuthUseCase: LoginAndInitializeAuthUseCase
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<LoginSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun loginWithGoogleAuthCode(authCode: String) {
        viewModelScope.launch {
            loginAndInitializeAuthUseCase(authCode)
                .onSuccess {
                    if(it.isRegistered) {
                        _sideEffect.emit(LoginSideEffect.ToastGreetingMessage(it.userNickname))
                        _sideEffect.emit(LoginSideEffect.NavigateToHome)
                    } else {
                        navigateToJoin(it.userEmail, it.userName)
                    }
                    Log.i(TAG, "로그인 성공: ${it.userEmail}")
                }.onFailure { e ->
                    if (e is ConflictException) {
                        emitLoginFailure()
                        Log.w(TAG, "구글 SSO 충돌: $e")
                    } else {
                        emitLoginServerError()
                        Log.e(TAG, "로그인 중 서버 에러", e)
                    }
                }
        }
    }

    private suspend fun navigateToJoin(email: String, name: String) {
        _sideEffect.emit(LoginSideEffect.NavigateToJoin(email, name))
    }

    private suspend fun emitLoginServerError() {
        _sideEffect.emit(LoginSideEffect.ToastLoginServerError)
    }

    private suspend fun emitLoginFailure() {
        _sideEffect.emit(LoginSideEffect.ToastLoginFailed)
    }

    fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val authCode = account?.serverAuthCode

            if(authCode.isNullOrEmpty()){
                emitGoogleSignInFailure(LoginSideEffect.ToastGoogleSignInFailed)
                Log.w(TAG, "GoogleSignIn 실패: 인증 코드 없음 (authCode=${authCode})")
                return
            }

            val encodedAuthCode = URLEncoder.encode(authCode, "UTF-8")
            loginWithGoogleAuthCode(encodedAuthCode)
            Log.i(TAG, "GoogleSignIn 성공: $authCode")

        } catch (e: ApiException) {
            emitGoogleSignInFailure(LoginSideEffect.ToastGoogleSignInError)
            Log.e(TAG, "GoogleSignIn 실패 ${e.statusCode}", e)
        }
    }

    private fun emitGoogleSignInFailure(sideEffect: LoginSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }
}