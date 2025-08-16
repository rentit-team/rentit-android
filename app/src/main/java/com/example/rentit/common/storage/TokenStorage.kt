package com.example.rentit.common.storage

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

private const val PREFS_NAME = "secure_prefs"
private const val ACCESS_TOKEN_KEY = "access_token"
private const val TAG = "SecurePrefs"

private fun getEncryptedSharedPreference(context: Context): SharedPreferences {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

fun saveToken(context: Context, token: String) {
    val sharedPreferences = getEncryptedSharedPreference(context)
    sharedPreferences.edit { putString(ACCESS_TOKEN_KEY, token) }
}

fun getToken(context: Context): String? {
    val sharedPreferences = getEncryptedSharedPreference(context)
    Log.w(TAG, "Failed to read token due to preference error")
    return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
}