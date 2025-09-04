package com.example.rentit.di

import com.example.rentit.data.chat.websocketImpl.WebSocketManagerImpl
import com.example.rentit.domain.chat.websocket.WebSocketManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {
    @Singleton
    @Provides
    fun provideWebSocketManager(webSocketManagerImpl: WebSocketManagerImpl): WebSocketManager = webSocketManagerImpl
}