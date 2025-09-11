package com.example.rentit.presentation.main.createpost.components

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.FileOutputStream
import java.io.File

fun createMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
    val file = uriToFile(context, uri) ?: return null

    val mimeType = context.contentResolver.getType(uri) ?: "image/*"
    val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())

    return MultipartBody.Part.createFormData(
        "thumbnailImg",
        file.name,
        requestBody
    )
}

private fun uriToFile(context: Context, uri: Uri): File? {
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val tempFile = File(context.cacheDir, fileName)

        inputStream?.use { input ->
            FileOutputStream(tempFile).use { input.copyTo(it) }
        }
        return tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}