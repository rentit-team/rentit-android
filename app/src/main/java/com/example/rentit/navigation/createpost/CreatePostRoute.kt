package com.example.rentit.navigation.createpost

import kotlinx.serialization.Serializable

sealed class CreatePostRoute {
    @Serializable
    data object CreatePost : CreatePostRoute()
}