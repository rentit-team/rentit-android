package com.example.rentit.presentation.auth.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.BuildConfig
import com.example.rentit.common.exception.user.GoogleSsoFailedException
import com.example.rentit.data.user.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import javax.inject.Inject

private const val TAG = "GoogleLogin"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<LoginSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun loginWithGoogleAuthCode(authCode: String) {
        viewModelScope.launch {
            repository.googleLogin(authCode, BuildConfig.GOOGLE_REDIRECT_URI)
                .onSuccess {
                    val name = it.data.user.name
                    if(it.data.isUserRegistered){
                        val token = it.data.accessToken.token
                        handleRegisteredUser(token, name)
                    } else {
                        val email = it.data.user.email
                        handleUnregisteredUser(email, name)
                    }
                    Log.d(TAG, "로그인 성공: $name")
                }
                .onFailure { e ->
                    if (e is GoogleSsoFailedException) {
                        emitAuthenticationFailure()
                        Log.w(TAG, "구글 SSO 충돌: $e")
                    } else {
                        emitLoginServerError()
                        Log.e(TAG, "서버 에러", e)
                    }
                }
        }
    }

    private suspend fun handleUnregisteredUser(email: String, name: String) {
        _sideEffect.emit(LoginSideEffect.NavigateToJoin(email, name))
    }

    private suspend fun handleRegisteredUser(token: String, name: String) {
        saveTokenFromPrefs(token)
        _sideEffect.emit(LoginSideEffect.ToastGreetingMessage(name))
        _sideEffect.emit(LoginSideEffect.NavigateToHome)
    }

    private suspend fun emitLoginServerError() {
        _sideEffect.emit(LoginSideEffect.ToastLoginServerError)
    }

    private suspend fun emitAuthenticationFailure() {
        _sideEffect.emit(LoginSideEffect.ToastAuthenticationFailed)
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
            Log.d(TAG, "GoogleSignIn 성공: $authCode")

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

    private fun saveTokenFromPrefs(token:String) = repository.saveTokenToPrefs(token)
}