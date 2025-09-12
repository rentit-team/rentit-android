package com.example.rentit.presentation.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.White
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
        bottomBar = { MainBottomBar(showBottomBar, currentRoute, onBottomItemClick) },
        floatingActionButton = { if (showCreatePostBtn) CreatePostFloatingButton(onCreatePostClick) }
    ) {
        MainNavHost(navHostController, it)
    }
}

@Composable
fun MainBottomBar(showBottomBar: Boolean, currentRoute: String?, onBottomItemClick: (String) -> Unit) {
    AnimatedVisibility(
        visible = showBottomBar,
        enter = slideInVertically { it },
    ) {
        NavigationBar(modifier = Modifier.fillMaxHeight(0.1f), containerColor = Color.White) {
            BottomNavItem.entries.forEach { item ->
                NavigationBarItem(
                    modifier = Modifier.fillMaxHeight(),
                    icon = {
                        Icon(
                            painter = painterResource(item.iconFor(currentRoute)),
                            contentDescription = stringResource(item.title),
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Gray400,
                        selectedIconColor = PrimaryBlue500,
                        indicatorColor = White
                    ),
                    selected = item.isSelected(currentRoute),
                    alwaysShowLabel = false,
                    onClick = { onBottomItemClick(item.screenRoute) },
                )
            }
        }
    }
}

@Composable
fun CreatePostFloatingButton(onClick: () -> Unit) {
    val contentDesc = stringResource(R.string.common_img_placeholder_description)

    FloatingActionButton(
        modifier = Modifier.semantics { contentDescription = contentDesc },
        shape = CircleShape,
        containerColor = PrimaryBlue500,
        onClick = onClick,
    ) {
        Image(
            modifier = Modifier.padding(14.dp),
            painter = painterResource(id = R.drawable.ic_write),
            contentDescription = null
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
