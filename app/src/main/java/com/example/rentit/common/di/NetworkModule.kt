package com.example.rentit.common.di

import android.content.Context
import android.util.Log
import com.example.rentit.common.storage.getToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val baseUrl = "http://api.rentit.o-r.kr:8080/"

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun provideOkHttpClient(tokenProvider: TokenProvider, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${tokenProvider.getAccessToken()}")
                    .build()
                it.proceed(request)
            }
            .build()

    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}


class TokenProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getAccessToken(): String {
        Log.d("TOKEN", "${getToken(context)}")
        return getToken(context) ?: ""
    }
}