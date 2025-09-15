package com.example.rentit.domain.user.usecase

import com.example.rentit.core.error.UnauthorizedException
import com.example.rentit.domain.auth.respository.AuthRepository
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

/**
 * 사용자 세션을 확인하는 UseCase
 *
 * - 로컬에 저장된 토큰과 사용자 ID를 확인
 * - 토큰이 없으면 MissingTokenException 발생 및 로컬 prefs 초기화
 * - 사용자 ID가 없으면 서버에서 가져와 로컬에 저장
 * - 앱 시작 시 사용자 인증/세션 상태 확인에 사용
 */

class CheckUserSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Long> {
        return runCatching {
            val token = authRepository.getAccessTokenFromPrefs()
            if(token.isNullOrEmpty()) throw UnauthorizedException()

            val localUserId = userRepository.getAuthUserIdFromPrefs()
            if(localUserId == -1L){
                val userId = userRepository.getMyInfo().getOrThrow().data.userId
                userRepository.saveAuthUserIdToPrefs(userId)
                userId // 로그 출력용
            } else {
                localUserId // 로그 출력용
            }
        }.onFailure { e ->
            if(e is UnauthorizedException) userRepository.clearPrefs()
        }
    }
}