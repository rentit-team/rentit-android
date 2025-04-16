package com.example.rentit.data.user

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoogleLoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> = _loginState

    fun startGoogleLogin(context: Context, activity: Activity, serverClientId: String) {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClientId)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(context)

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = activity
                )

                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken
                    _loginState.value = LoginResult.Success(idToken)
                } else {
                    _loginState.value = LoginResult.Failure("Unexpected credential type")
                }
            } catch (e: GetCredentialException) {
                Log.e("GoogleLogin", "Credential error: ${e.message}", e)
                _loginState.value = LoginResult.Failure("로그인 실패: ${e.message}")
            } catch (e: GoogleIdTokenParsingException) {
                _loginState.value = LoginResult.Failure("ID 토큰 파싱 오류")
            }
        }
    }
}

sealed class LoginResult {
    data object Idle : LoginResult()
    data class Success(val token: String) : LoginResult()
    data class Failure(val message: String) : LoginResult()
}
