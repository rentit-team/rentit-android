package com.example.rentit.data.auth.local

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

private const val REFRESH_TOKEN_KEY = "refresh_token"
private const val ACCESS_TOKEN_KEY = "access_token"

class AuthPrefsDataSource @Inject constructor(
    private val sharedPrefs: SharedPreferences,
) {
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