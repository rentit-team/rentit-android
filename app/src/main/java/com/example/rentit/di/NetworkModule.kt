package com.example.rentit.di

import com.example.rentit.data.user.local.UserPrefsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://api.rentit.o-r.kr:8080/"

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    /** Default OkHttpClient */

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        userPrefsDataSource: UserPrefsDataSource
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${userPrefsDataSource.getAccessTokenFromPrefs()}")
                    .build()
                it.proceed(request)
            }
            .build()

    @Provides
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
    fun providePhotoUploadOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        userPrefsDataSource: UserPrefsDataSource
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC))
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${userPrefsDataSource.getAccessTokenFromPrefs()}")
                    .build()
                it.proceed(request)
            }
            .build()

    @UploadOkHttpClient
    @Provides
    fun providePhotoUploadRetrofit(
        @UploadOkHttpClient client: OkHttpClient
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