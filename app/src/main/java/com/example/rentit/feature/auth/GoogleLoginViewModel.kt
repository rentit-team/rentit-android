package com.example.rentit.feature.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.rentit.data.user.model.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GoogleLoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> = _loginState

    fun handleSignInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                // 인증 코드 가져오기
                val authCode = account?.serverAuthCode
                if (authCode != null) {
                    _loginState.value = LoginResult.Success(authCode)
                } else {
                    _loginState.value = LoginResult.Failure("인증 코드 가져오기 실패")
                }
            } catch (e: ApiException) {
                _loginState.value = LoginResult.Failure("로그인 실패: ${e.message}")
            }
        }
    }
}

private const val RC_SIGN_IN = 9001
