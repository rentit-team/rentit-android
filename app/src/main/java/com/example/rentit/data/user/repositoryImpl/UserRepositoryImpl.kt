package com.example.rentit.data.user.repositoryImpl

import com.example.rentit.common.exception.MissingTokenException
import com.example.rentit.common.exception.ServerException
import com.example.rentit.domain.rental.exception.EmptyBodyException
import com.example.rentit.domain.rental.exception.TooManyRequestException
import com.example.rentit.domain.rental.exception.VerificationFailedException
import com.example.rentit.domain.rental.exception.VerificationRequestNotFoundException
import com.example.rentit.domain.user.exception.GoogleSsoFailedException
import com.example.rentit.data.user.dto.GoogleLoginResponseDto
import com.example.rentit.data.user.dto.MyInfoResponseDto
import com.example.rentit.data.user.dto.MyProductListResponseDto
import com.example.rentit.data.user.dto.MyRentalListResponseDto
import com.example.rentit.data.user.dto.SendPhoneCodeResponseDto
import com.example.rentit.data.user.local.UserPrefsDataSource
import com.example.rentit.data.user.remote.UserRemoteDataSource
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val prefsDataSource: UserPrefsDataSource,
    private val remoteDataSource: UserRemoteDataSource
): UserRepository {
    override suspend fun googleLogin(code: String, redirectUri: String): Result<GoogleLoginResponseDto> {
        return runCatching {
            val response = remoteDataSource.googleLogin(code, redirectUri)
            if (response.isSuccessful) {
                response.body() ?: throw EmptyBodyException("Empty response body")
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Client error"
                throw if (response.code() == 409) GoogleSsoFailedException(errorMsg) else ServerException(errorMsg)
            }
        }
    }

    override suspend fun sendPhoneCode(phoneNumber: String): Result<SendPhoneCodeResponseDto> {
        return runCatching {
            val response = remoteDataSource.sendPhoneCode(phoneNumber)
            if(response.isSuccessful) {
                response.body() ?: throw EmptyBodyException()
            } else {
                throw if (response.code() == 403) TooManyRequestException() else ServerException()
            }
        }
    }

    override suspend fun verifyPhoneCode(phoneNumber: String, code: String): Result<Unit> {
        return runCatching {
            val response = remoteDataSource.verifyPhoneCode(phoneNumber, code)
            if (response.isSuccessful) {
                response.body()?.let {
                    if (!it.verified) throw VerificationFailedException(it.message)
                } ?: throw EmptyBodyException()
            } else {
                throw if (response.code() == 403) VerificationRequestNotFoundException() else ServerException()
            }
        }
    }

    override suspend fun signUp(name: String, email: String, nickname: String, profileImageUrl: String): Result<Unit> {
        return try {
            val response = remoteDataSource.signUp(name, email, nickname, profileImageUrl)
            when(response.code()) {
                200 -> {
                    val body = response.body()
                    if(body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                }
                409 -> {
                    val errorMsg = response.errorBody()?.string() ?: "Client error"
                    Result.failure(Exception("Client error: $errorMsg"))
                }
                500 -> {
                    Result.failure(Exception("Server error"))
                }
                else -> {
                    Result.failure(Exception("Unexpected error"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyInfo(): Result<MyInfoResponseDto> {
        return runCatching {
            val response = remoteDataSource.getMyInfo()
            if(response.isSuccessful) {
                response.body() ?: throw EmptyBodyException("Empty response body")
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Client error"
                throw if(response.code() == 401) MissingTokenException(errorMsg) else ServerException(errorMsg)
            }
        }
    }

    override suspend fun getMyProductList(): Result<MyProductListResponseDto> {
        return try {
            val response = remoteDataSource.getMyProductList()
            when(response.code()) {
                200 -> {
                    val body = response.body()
                    if(body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                }
                500 -> {
                    Result.failure(Exception("Server error"))
                }
                else -> {
                    Result.failure(Exception("Unexpected error"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyRentalList(): Result<MyRentalListResponseDto> {
        return try {
            val response = remoteDataSource.getMyRentalList()
            when(response.code()) {
                200 -> {
                    val body = response.body()
                    if(body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                }
                500 -> {
                    Result.failure(ServerException())
                }
                else -> {
                    Result.failure(Exception("Unexpected error"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAuthUserIdFromPrefs(): Long = prefsDataSource.getAuthUserIdFromPrefs()

    override fun saveAuthUserIdToPrefs(authUserId: Long) = prefsDataSource.saveAuthUserIdToPrefs(authUserId)

    override fun getTokenFromPrefs(): String? = prefsDataSource.getTokenFromPrefs()

    override fun saveTokenToPrefs(token: String) = prefsDataSource.saveTokenToPrefs(token)

    override fun clearPrefs() = prefsDataSource.clearPrefs()
}
