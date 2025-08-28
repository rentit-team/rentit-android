package com.example.rentit.data.user.usecase

import com.example.rentit.common.exception.MissingTokenException
import com.example.rentit.data.user.repository.UserRepository
import javax.inject.Inject

class CheckUserSessionUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Long> {
        return runCatching {
            val token = userRepository.getTokenFromPrefs()
            if(token.isNullOrEmpty()) throw MissingTokenException()

            val localUserId = userRepository.getAuthUserIdFromPrefs()
            if(localUserId == -1L){
                val userId = userRepository.getMyInfo().getOrThrow().data.userId
                userRepository.saveAuthUserIdToPrefs(userId)
                userId // 로그 출력용
            } else {
                localUserId // 로그 출력용
            }
        }.onFailure { e ->
            if(e is MissingTokenException) userRepository.clearPrefs()
        }
    }
}