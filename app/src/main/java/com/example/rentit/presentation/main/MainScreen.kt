package com.example.rentit.presentation.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.navigation.MainNavHost


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navHostController: NavHostController,
    currentRoute: String?,
    showBottomBar: Boolean,
    showCreatePostBtn: Boolean,
    onBottomItemClick: (String) -> Unit = {},
    onCreatePostClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = { if (showBottomBar) MainBottomBar(currentRoute, onBottomItemClick) },
        floatingActionButton = { if (showCreatePostBtn) CreatePostFloatingButton(onCreatePostClick) }
    ) {
        MainNavHost(navHostController, it)
    }
}

@Composable
fun MainBottomBar(currentRoute: String?, onBottomItemClick: (String) -> Unit) {
    BottomNavigation(backgroundColor = Color.White, modifier = Modifier.height(72.dp)) {
        BottomNavItem.entries.forEach { item ->
            BottomNavigationItem(
                modifier = Modifier.fillMaxHeight(),
                icon = {
                    Icon(
                        painter = painterResource(item.iconFor(currentRoute)),
                        contentDescription = stringResource(item.title),
                        modifier = Modifier.size(28.dp)
                    )
                },
                unselectedContentColor = Gray400,
                selectedContentColor = PrimaryBlue500,
                selected = item.isSelected(currentRoute),
                alwaysShowLabel = false,
                onClick = { onBottomItemClick(item.screenRoute) },
            )
        }
    }
}

@Composable
fun CreatePostFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        backgroundColor = PrimaryBlue500,
        onClick = onClick,
    ) {
        Image(
            modifier = Modifier.padding(14.dp),
            painter = painterResource(id = R.drawable.ic_write),
            contentDescription = "상품 등록하기"
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMainView() {
    RentItTheme {
        MainScreen(
            rememberNavController(),
            currentRoute = BottomNavItem.Home.screenRoute,
            showBottomBar = true,
            showCreatePostBtn = true
        )
    }
}
