package com.example.rentit.navigation

import androidx.navigation.NavHostController

fun moveScreen(
    navController: NavHostController,
    route: String,
    saveStateEnabled: Boolean = false,
    isInclusive: Boolean = false,
    restoreStateEnabled: Boolean = false
) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let {
            popUpTo(it) {
                saveState = saveStateEnabled  // Stack에서 제거되는 화면의 상태 저장
                inclusive = isInclusive    // 특정(현재)화면까지 스택에서 제거
            }
        }
        launchSingleTop = true  // 같은 화면을 여러 번 쌓지 않도록
        restoreState = restoreStateEnabled     // 이전에 방문한 화면이라면 저장된 상태 복원
    }
}