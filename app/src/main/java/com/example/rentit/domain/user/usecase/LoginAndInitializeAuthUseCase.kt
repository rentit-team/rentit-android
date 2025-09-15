package com.example.rentit.domain.user.usecase

import com.example.rentit.BuildConfig
import com.example.rentit.domain.auth.respository.AuthRepository
import com.example.rentit.domain.user.model.LoginResultModel
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

/**
 * 로그인 후 사용자 정보 초기화를 처리하는 UseCase
 *
 * - 이미 등록된 유저인 경우
 *   - 서버에서 발급받은 토큰을 로컬에 저장
 *   - 서버에서 사용자 정보를 조회하여 사용자 ID를 로컬에 저장
 * - 성공 시 로그인 정보 (LoginResultModel) 리턴
 * - 실패 시 로컬 prefs 초기화
 */

class LoginAndInitializeAuthUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(authCode: String): Result<LoginResultModel> {
        return runCatching {
            val response = userRepository.googleLogin(authCode, BuildConfig.GOOGLE_REDIRECT_URI).getOrThrow()
            val data = response.data

            if(data.isUserRegistered) {
                authRepository.saveRefreshTokenToPrefs(data.refreshToken.token)
                authRepository.saveAccessTokenToPrefs(data.accessToken.token)

                val userData = userRepository.getMyInfo().getOrThrow().data
                userRepository.saveAuthUserIdToPrefs(userData.userId)
                userRepository.saveAuthNicknameToPrefs(userData.nickname)
            }

            val nickname = userRepository.getAuthNicknameFromPrefs()

            LoginResultModel(
                isRegistered = data.isUserRegistered,
                userNickname = nickname,
                userName = data.user.name,
                userEmail = data.user.email,
            )
        }.onFailure {
            userRepository.clearPrefs()
        }
    }
}