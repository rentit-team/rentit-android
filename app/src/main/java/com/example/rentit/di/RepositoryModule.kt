package com.example.rentit.di

import com.example.rentit.data.auth.repositoryImpl.AuthRepositoryImpl
import com.example.rentit.data.chat.repositoryImpl.ChatRepositoryImpl
import com.example.rentit.data.product.repositoryImpl.ProductRepositoryImpl
import com.example.rentit.data.rental.repositoryImpl.RentalRepositoryImpl
import com.example.rentit.data.user.repositoryImpl.UserRepositoryImpl
import com.example.rentit.domain.auth.respository.AuthRepository
import com.example.rentit.domain.chat.repository.ChatRepository
import com.example.rentit.domain.product.repository.ProductRepository
import com.example.rentit.domain.rental.repository.RentalRepository
import com.example.rentit.domain.user.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository = authRepositoryImpl

    @Provides
    @Singleton
    fun provideChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository = chatRepositoryImpl

    @Provides
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository = userRepositoryImpl

    @Provides
    @Singleton
    fun provideProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository = productRepositoryImpl

    @Provides
    @Singleton
    fun provideRentalRepository(rentalRepositoryImpl: RentalRepositoryImpl): RentalRepository = rentalRepositoryImpl
}