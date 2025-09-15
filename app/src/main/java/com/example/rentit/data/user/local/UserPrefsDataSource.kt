package com.example.rentit.data.user.local

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

private const val KEY_AUTH_USER_ID = "auth_user_id"
private const val REFRESH_TOKEN_KEY = "refresh_token"
private const val ACCESS_TOKEN_KEY = "access_token"

class UserPrefsDataSource @Inject constructor(
    private val sharedPrefs: SharedPreferences,
) {
    fun saveAuthUserIdToPrefs(authUserId: Long) {
        sharedPrefs.edit { putLong(KEY_AUTH_USER_ID, authUserId) }
    }

    fun getAuthUserIdFromPrefs(): Long = sharedPrefs.getLong(KEY_AUTH_USER_ID, -1)

    fun saveRefreshTokenToPrefs(token: String) {
        sharedPrefs.edit { putString(REFRESH_TOKEN_KEY, token) }
    }

    fun getRefreshTokenFromPrefs(): String? = sharedPrefs.getString(REFRESH_TOKEN_KEY, null)

    fun saveAccessTokenToPrefs(token: String) {
        sharedPrefs.edit { putString(ACCESS_TOKEN_KEY, token) }
    }

    fun getAccessTokenFromPrefs(): String? = sharedPrefs.getString(ACCESS_TOKEN_KEY, null)

    fun clearPrefs() { sharedPrefs.edit { clear() } }
}