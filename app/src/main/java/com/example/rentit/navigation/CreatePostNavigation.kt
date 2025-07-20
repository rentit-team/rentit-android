package com.example.rentit.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.rentit.presentation.home.createpost.CreatePostScreen

fun NavHostController.navigateToCreatePost() {
    navigate(
        route = CreatePostRoute.CreatePost
    )
}

fun NavGraphBuilder.createPostGraph(navHostController: NavHostController) {
    composable<CreatePostRoute.CreatePost> { CreatePostScreen(navHostController) }
}