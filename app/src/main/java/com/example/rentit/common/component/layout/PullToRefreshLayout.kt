package com.example.rentit.common.component.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.rentit.common.theme.PrimaryBlue500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshLayout(
    isRefreshing: Boolean,
    pullToRefreshState: PullToRefreshState,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        indicator = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier.align(Alignment.Center),
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    color = PrimaryBlue500,
                )
            }
        }
    ) {
        content()
    }
}