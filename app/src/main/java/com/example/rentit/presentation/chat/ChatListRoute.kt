package com.example.rentit.presentation.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.navigation.chatroom.navigateToChatRoom
import com.example.rentit.presentation.main.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListRoute(navHostController: NavHostController) {
    val backStackEntry = navHostController.currentBackStackEntry
    val mainViewModel: MainViewModel? = backStackEntry?.let { hiltViewModel(it) }

    val viewModel: ChatListViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchChatRoomSummaries()
        mainViewModel?.setRetryAction(viewModel::retryFetchChatRoomSummaries)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                if(it is ChatListSideEffect.CommonError) {
                    mainViewModel?.handleError(it.throwable)
                }
            }
        }
    }

    ChatListScreen(
        chatRoomSummaries = uiState.activeChatRoomSummaries,
        isActiveChatRooms = uiState.isActiveChatRooms,
        scrollState = uiState.scrollState,
        onToggleFilter = viewModel::onToggledFilter,
        onItemClick = { navHostController.navigateToChatRoom(it) }
    )

    LoadingScreen(uiState.isLoading)
}