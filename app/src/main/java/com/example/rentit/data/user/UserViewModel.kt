package com.example.rentit.data.user

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.GOOGLE_REDIRECT_URI
import com.example.rentit.data.user.dto.LoginResponseDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var googleLoginResult by mutableStateOf<Result<Response<LoginResponseDto>>?>(null)
    fun onGoogleLogin(code: String) {
        viewModelScope.launch {
            googleLoginResult = repository.googleLogin(code, GOOGLE_REDIRECT_URI)
        }
    }
}
