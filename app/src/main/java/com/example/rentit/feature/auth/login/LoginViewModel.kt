package com.example.rentit.feature.auth.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.GOOGLE_REDIRECT_URI
import com.example.rentit.data.user.dto.GoogleLoginResponseDto
import com.example.rentit.data.user.dto.UserDto
import com.example.rentit.data.user.model.GoogleSignInResult
import com.example.rentit.data.user.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val RC_SIGN_IN = 9001

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _googleSignInState = MutableStateFlow<GoogleSignInResult>(GoogleSignInResult.Idle)
    val googleSignInState: StateFlow<GoogleSignInResult> = _googleSignInState

    private val _googleLoginResult = MutableStateFlow<Result<GoogleLoginResponseDto>?>(null)
    val googleLoginResult: StateFlow<Result<GoogleLoginResponseDto>?> = _googleLoginResult

    private val _userData = MutableStateFlow<UserDto?>(null)
    val userData: StateFlow<UserDto?> = _userData

    fun onGoogleLogin(code: String) {
        viewModelScope.launch {
            _googleLoginResult.value = repository.googleLogin(code, GOOGLE_REDIRECT_URI)
                .onSuccess { _userData.value = it.data.user }
        }
    }

    fun handleGoogleSignInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                // 인증 코드 가져오기
                val authCode = account?.serverAuthCode
                if (authCode != null) {
                    _googleSignInState.value = GoogleSignInResult.Success(authCode)
                } else {
                    _googleSignInState.value = GoogleSignInResult.Failure("인증 코드 가져오기 실패")
                }
            } catch (e: ApiException) {
                _googleSignInState.value = GoogleSignInResult.Failure("로그인 실패: ${e.message}")
            }
        }
    }
}