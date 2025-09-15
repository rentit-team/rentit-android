package com.example.rentit.core.network

import com.example.rentit.domain.auth.respository.AuthRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor (
    private val authRepository: AuthRepository
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = authRepository.getAccessTokenFromPrefs()
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(request)
    }
}