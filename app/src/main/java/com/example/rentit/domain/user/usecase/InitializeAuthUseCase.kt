package com.example.rentit.domain.user.usecase

import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

/**
 * 사용자 인증 초기화를 처리하는 UseCase
 *
 * - 서버에서 발급받은 토큰을 로컬에 저장
 * - 서버에서 사용자 정보를 조회하여 사용자 ID를 로컬에 저장
 * - 실패 시 로컬 prefs 초기화
 */

class InitializeAuthUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(token: String): Result<Unit> {
        return runCatching {
            userRepository.saveTokenToPrefs(token)
            val userId = userRepository.getMyInfo().getOrThrow().data.userId
            userRepository.saveAuthUserIdToPrefs(userId)
        }.onFailure {
            userRepository.clearPrefs()
        }
    }
}