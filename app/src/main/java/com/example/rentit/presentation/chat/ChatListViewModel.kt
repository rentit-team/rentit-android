package com.example.rentit.presentation.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.chat.usecase.GetChatRoomSummariesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getChatRoomSummariesUseCase: GetChatRoomSummariesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListState())
    val uiState: StateFlow<ChatListState> = _uiState

    init {
        fetchChatRoomSummaries()
    }

    private fun fetchChatRoomSummaries() {
        viewModelScope.launch {
            setIsLoading(true)
            getChatRoomSummariesUseCase.invoke()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(chatRoomSummaries = it)
                }
                .onFailure { e ->
                    when (e) {
                        is IOException -> {
                            showNetworkErrorDialog()
                        }
                        else -> {
                            showServerErrorDialog()
                        }
                        // TODO: 토큰 에러 처리 필요 (리프레시 토큰으로 재발급 또는 로그인 화면 이동)
                    }
                }
            setIsLoading(false)
        }
    }

    private fun setIsLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    private fun showServerErrorDialog() {
        _uiState.value = _uiState.value.copy(showServerErrorDialog = true)
    }

    private fun showNetworkErrorDialog() {
        _uiState.value = _uiState.value.copy(showNetworkErrorDialog = true)
    }

    fun retryFetchChatRoomSummaries(){
        _uiState.value = _uiState.value.copy(showServerErrorDialog = false, showNetworkErrorDialog = false)
        fetchChatRoomSummaries()
    }
}