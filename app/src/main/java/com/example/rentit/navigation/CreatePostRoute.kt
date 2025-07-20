package com.example.rentit.navigation

import kotlinx.serialization.Serializable

sealed class CreatePostRoute {
    @Serializable
    data object CreatePost : CreatePostRoute()
}