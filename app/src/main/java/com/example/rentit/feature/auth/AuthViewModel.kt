package com.example.rentit.feature.auth

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.GOOGLE_REDIRECT_URI
import com.example.rentit.data.user.repository.UserRepository
import com.example.rentit.data.user.dto.GoogleLoginResponseDto
import com.example.rentit.data.user.model.GoogleSignInResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var googleLoginResult by mutableStateOf<Result<GoogleLoginResponseDto>?>(null)

    private val _googleSignInState = MutableStateFlow<GoogleSignInResult>(GoogleSignInResult.Idle)
    val googleSignInState: StateFlow<GoogleSignInResult> = _googleSignInState

    fun onGoogleLogin(code: String) {
        viewModelScope.launch {
            googleLoginResult = repository.googleLogin(code, GOOGLE_REDIRECT_URI)
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

private const val RC_SIGN_IN = 9001