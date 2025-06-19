package com.example.rentit.common.storage

import android.content.Context
import android.util.Log
import androidx.core.content.edit

private const val PREFS_NAME = "my_prefs"
private const val MY_ID_KEY = "my_id"
private const val TAG = "MyPrefs"

fun saveMyIdToPrefs(context: Context, myId: Long) {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putLong(MY_ID_KEY, myId)
    }
}

fun getMyIdFromPrefs(context: Context): Long {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val myId = sharedPreferences.getLong(MY_ID_KEY, -1)
    Log.d(TAG, "myId: $myId")
    return myId
}