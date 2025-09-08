package com.example.rentit.presentation.auth.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.BuildConfig
import com.example.rentit.core.error.ConflictException
import com.example.rentit.core.error.UnauthorizedException
import com.example.rentit.domain.user.repository.UserRepository
import com.example.rentit.domain.user.usecase.InitializeAuthUseCase
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
    private val userRepository: UserRepository,
    private val initializeAuthUseCase: InitializeAuthUseCase
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<LoginSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun loginWithGoogleAuthCode(authCode: String) {
        viewModelScope.launch {
            userRepository.googleLogin(authCode, BuildConfig.GOOGLE_REDIRECT_URI)
                .onSuccess {
                    val userName = it.data.user.name
                    if(it.data.isUserRegistered){
                        handleAuthenticatedUser(it.data.accessToken.token, userName)
                    } else {
                        navigateToJoin(it.data.user.email, userName)
                    }
                    Log.i(TAG, "로그인 성공: $userName")
                }
                .onFailure { e ->
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

    private suspend fun handleAuthenticatedUser(token: String, name: String) {
        initializeAuthUseCase.invoke(token)
            .onSuccess {
                _sideEffect.emit(LoginSideEffect.ToastGreetingMessage(name))
                _sideEffect.emit(LoginSideEffect.NavigateToHome)
            }.onFailure { e ->
                if (e is UnauthorizedException) {
                    emitLoginFailure()
                    Log.w(TAG, "유효하지 않은 토큰으로 사용자 정보 조회 실패: $e")
                } else {
                    emitLoginServerError()
                    Log.e(TAG, "사용자 정보 조회 중 서버 에러", e)
                }
            }
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