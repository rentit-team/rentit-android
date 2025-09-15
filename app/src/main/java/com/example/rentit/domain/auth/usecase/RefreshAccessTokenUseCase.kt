package com.example.rentit.domain.auth.usecase

import com.example.rentit.domain.auth.respository.AuthRepository
import javax.inject.Inject

/**
 * 토큰 재발급하고 실패 시 로그아웃 처리(로컬 prefs 초기화)하는 UseCase
 */

class RefreshAccessTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<String> {
        return runCatching {
            authRepository.refreshAccessToken().getOrThrow()
        }.onSuccess {
            authRepository.saveAccessTokenToPrefs(it)
        }.onFailure {
            authRepository.clearPrefs()
        }
    }
}