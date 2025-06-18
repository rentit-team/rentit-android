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

private fun getEncryptedSharedPreference(context: Context): SharedPreferences? {
    return try {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch(e: Exception) {
        Log.e(TAG, "[${e::class.simpleName}] Failed to create EncryptedSharedPreferences")
        context.deleteSharedPreferences(PREFS_NAME)
        null
    }
}

fun saveToken(context: Context, token: String) {
    val sharedPreferences = getEncryptedSharedPreference(context)
    sharedPreferences?.edit { putString(ACCESS_TOKEN_KEY, token) }
        ?: Log.w(TAG, "Token not saved due to preference error")
}

fun getToken(context: Context): String? {
    val sharedPreferences = getEncryptedSharedPreference(context)
    if(sharedPreferences == null) Log.w(TAG, "Failed to read token due to preference error")
    return sharedPreferences?.getString(ACCESS_TOKEN_KEY, null)
}