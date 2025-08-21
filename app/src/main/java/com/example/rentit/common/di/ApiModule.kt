package com.example.rentit.common.di

import com.example.rentit.data.chat.remote.ChatApiService
import com.example.rentit.data.product.remote.ProductApiService
import com.example.rentit.data.rental.remote.PhotoUploadApiService
import com.example.rentit.data.rental.remote.RentalApiService
import com.example.rentit.data.user.remote.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideChatApiService(retrofit: Retrofit): ChatApiService {
        return retrofit.create(ChatApiService::class.java)
    }

    @Provides
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    fun provideProductApiService(retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }

    @Provides
    fun provideRentalApiService(retrofit: Retrofit): RentalApiService {
        return retrofit.create(RentalApiService::class.java)
    }

    @Provides
    fun providePhotoUploadApiService(@UploadOkHttpClient retrofit: Retrofit): PhotoUploadApiService {
        return retrofit.create(PhotoUploadApiService::class.java)
    }
}