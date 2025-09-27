package com.example.rentit.data.auth

import android.util.Log
import com.example.rentit.domain.auth.usecase.RefreshAccessTokenUseCase
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

private const val TAG = "TokenAuthenticator"

class TokenAuthenticator @Inject constructor (
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase
): Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        if(response.request.header("Authorization-Retry") != null) {
            Log.i(TAG, "이미 시도한 토큰 갱신 요청으로 재시도 하지 않음")
            return null
        }

        val newAccessToken = runBlocking {
            refreshAccessTokenUseCase()
                .onSuccess {
                    Log.i(TAG, "액세스 토큰 갱신 성공, 요청 재시도")
                }.onFailure { e ->
                    Log.e(TAG, "액세스 토큰 갱신 실패", e)
                }.getOrNull()
        } ?: return null


        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .header("Authorization-Retry", "true")
            .build()
    }
}