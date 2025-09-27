package com.example.rentit.presentation.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.common.ui.component.layout.RentItLoadingScreen
import com.example.rentit.navigation.chatroom.navigateToChatRoom
import com.example.rentit.presentation.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListRoute(navHostController: NavHostController) {
    val mainViewModel: MainViewModel = hiltViewModel()

    val viewModel: ChatListViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.fetchChatRoomSummaries()
        mainViewModel.setRetryAction(viewModel::retryFetchChatRoomSummaries)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                if(it is ChatListSideEffect.CommonError) {
                    mainViewModel.handleError(it.throwable)
                }
            }
        }
    }

    ChatListScreen(
        chatRoomSummaries = uiState.activeChatRoomSummaries,
        scrollState = uiState.scrollState,
        pullToRefreshState = pullToRefreshState,
        isActiveChatRooms = uiState.isActiveChatRooms,
        isRefreshing = uiState.isRefreshing,
        onRefresh = viewModel::refreshChatRoomSummaries,
        onToggleFilter = viewModel::onToggledFilter,
        onItemClick = { navHostController.navigateToChatRoom(it) }
    )

    RentItLoadingScreen(uiState.isLoading)
}