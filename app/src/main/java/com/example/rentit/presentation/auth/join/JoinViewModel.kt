package com.example.rentit.presentation.auth.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _signUpResult = MutableStateFlow<Result<Unit>?>(null)
    val signUpResult: StateFlow<Result<Unit>?> = _signUpResult

    fun onSignUp(name: String = "", email: String, nickname: String, profileImageUrl: String = "") {
        viewModelScope.launch {
            repository.signUp(name, email, nickname, profileImageUrl)
        }
    }
}