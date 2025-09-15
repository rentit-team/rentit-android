package com.example.rentit.di

import com.example.rentit.core.network.AccessTokenInterceptor
import com.example.rentit.core.network.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://api.rentit.o-r.kr:8080/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(tokenAuthenticator: TokenAuthenticator): Authenticator = tokenAuthenticator

    @Provides
    @Singleton
    fun provideAuthInterceptor(accessTokenInterceptor: AccessTokenInterceptor): Interceptor = accessTokenInterceptor

    /** Default OkHttpClient */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        accessTokenInterceptor: AccessTokenInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(accessTokenInterceptor)
            .authenticator(tokenAuthenticator)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /** Photo Upload OkHttpClient */
    @UploadOkHttpClient
    @Provides
    @Singleton
    fun providePhotoUploadOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        accessTokenInterceptor: AccessTokenInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(accessTokenInterceptor)
            .authenticator(tokenAuthenticator)
            .build()

    @UploadOkHttpClient
    @Provides
    @Singleton
    fun providePhotoUploadRetrofit(
        @UploadOkHttpClient client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /** Refresh Token OkHttpClient */
    @RefreshTokenOkHttpClient
    @Provides
    @Singleton
    fun provideRefreshTokenOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        accessTokenInterceptor: AccessTokenInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC))
            .addInterceptor(accessTokenInterceptor)
            .build()

    @RefreshTokenOkHttpClient
    @Provides
    @Singleton
    fun provideRefreshTokenRetrofit(
        @RefreshTokenOkHttpClient client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UploadOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RefreshTokenOkHttpClient