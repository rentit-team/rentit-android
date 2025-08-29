package com.example.rentit.domain.user.usecase

import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

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