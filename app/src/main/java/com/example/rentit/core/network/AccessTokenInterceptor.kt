package com.example.rentit.core.network

import com.example.rentit.data.auth.local.AuthPrefsDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor (
    private val prefsDataSource: AuthPrefsDataSource
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken: String? = prefsDataSource.getAccessTokenFromPrefs()

        if(accessToken.isNullOrEmpty()) return chain.proceed(chain.request())

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        return chain.proceed(request)
    }
}