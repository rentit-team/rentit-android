package com.example.rentit.navigation.createpost

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.rentit.presentation.main.createpost.CreatePostRoute

fun NavHostController.navigateToCreatePost() {
    navigate(
        route = CreatePostRoute.CreatePost
    )
}

fun NavGraphBuilder.createPostGraph(navHostController: NavHostController) {
    composable<CreatePostRoute.CreatePost> { CreatePostRoute(navHostController) }
}