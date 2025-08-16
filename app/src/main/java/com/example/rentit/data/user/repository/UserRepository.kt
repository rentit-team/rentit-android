package com.example.rentit.data.user.repository

import com.example.rentit.common.exception.ServerException
import com.example.rentit.data.user.dto.GoogleLoginResponseDto
import com.example.rentit.data.user.dto.MyInfoResponseDto
import com.example.rentit.data.user.dto.MyProductListResponseDto
import com.example.rentit.data.user.dto.MyRentalListResponseDto
import com.example.rentit.data.user.local.UserPrefsDataSource
import com.example.rentit.data.user.remote.UserRemoteDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val prefsDataSource: UserPrefsDataSource,
    private val remoteDataSource: UserRemoteDataSource
) {
    suspend fun googleLogin(code: String, redirectUri: String): Result<GoogleLoginResponseDto> {
        return try {
            val response = remoteDataSource.googleLogin(code, redirectUri)
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

    suspend fun signUp(name: String, email: String, nickname: String, profileImageUrl: String): Result<Unit> {
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

    suspend fun getMyInfo(): Result<MyInfoResponseDto> {
        return try {
            val response = remoteDataSource.getMyInfo()
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

    suspend fun getMyProductList(): Result<MyProductListResponseDto> {
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

    suspend fun getMyRentalList(): Result<MyRentalListResponseDto> {
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

    fun getAuthUserIdFromPrefs(): Long = prefsDataSource.getAuthUserIdFromPrefs()

    fun saveAuthUserIdToPrefs(authUserId: Long) = prefsDataSource.saveAuthUserIdToPrefs(authUserId)

    fun getTokenFromPrefs(): String? = prefsDataSource.getTokenFromPrefs()

    fun saveTokenToPrefs(token: String) = prefsDataSource.saveTokenToPrefs(token)
}
