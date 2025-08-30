package com.example.rentit.presentation.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.chat.model.ChatRoomSummaryModel
import com.example.rentit.domain.chat.usecase.GetChatRoomSummariesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getChatRoomSummariesUseCase: GetChatRoomSummariesUseCase
) : ViewModel() {

    private val _chatRoomSummaries = MutableStateFlow<List<ChatRoomSummaryModel>>(emptyList())
    val chatRoomSummaries: StateFlow<List<ChatRoomSummaryModel>> = _chatRoomSummaries

    fun fetchChatRoomList() {
        viewModelScope.launch {
            getChatRoomSummariesUseCase.invoke()
                .onSuccess {
                    _chatRoomSummaries.value = it
                }
                .onFailure { e ->

                }
        }
    }
}