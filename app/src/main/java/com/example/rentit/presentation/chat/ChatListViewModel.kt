package com.example.rentit.presentation.chat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.chat.usecase.GetChatRoomSummariesUseCase
import com.example.rentit.presentation.chat.model.ChatListFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ChatListViewModel"

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getChatRoomSummariesUseCase: GetChatRoomSummariesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListState())
    val uiState: StateFlow<ChatListState> = _uiState

    private val _sideEffect = MutableSharedFlow<ChatListSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun updateUiState(transform: ChatListState.() -> ChatListState) {
        _uiState.value = transform(_uiState.value)
    }

    suspend fun fetchChatRoomSummaries() {
        setIsLoading(true)
        getChatRoomSummariesUseCase()
            .onSuccess {
                updateUiState { copy(chatRoomSummaries = it) }
                Log.i(TAG, "채팅 리스트 가져오기 성공: 채팅방 ${it.size}개")
            }
            .onFailure { e ->
                Log.e(TAG, "채팅 리스트 가져오기 실패", e)
                _sideEffect.emit(ChatListSideEffect.CommonError(e))
            }
        setIsLoading(false)
    }

    private fun setIsLoading(isLoading: Boolean) {
        updateUiState { copy(isLoading = isLoading) }
    }

    fun retryFetchChatRoomSummaries(){
        viewModelScope.launch {
            fetchChatRoomSummaries()
        }
    }

    fun onToggledFilter(filterMode: ChatListFilter) {
        when(filterMode) {
            ChatListFilter.ACTIVE -> updateUiState { copy(isActiveChatRooms = true) }
            ChatListFilter.EMPTY -> updateUiState { copy(isActiveChatRooms = false) }
        }
    }
}