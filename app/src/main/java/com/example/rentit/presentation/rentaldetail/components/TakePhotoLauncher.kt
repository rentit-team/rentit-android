package com.example.rentit.presentation.rentaldetail.components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.rentit.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 사진 촬영 후 결과를 받기 위한 Launcher
 */
@Composable
fun rememberTakePhotoLauncher(
    onTakePhotoSuccess: (Uri) -> Unit,
): () -> Unit {
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = photoUri
        if (success && uri != null) {
            onTakePhotoSuccess(uri)
        }
    }

    val launchCameraWithNewPhotoUri: () -> Unit = {
        val uri = createPhotoUri(context)
        if(uri != null){
            photoUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, context.getString(R.string.toast_camera_launcher_fail), Toast.LENGTH_SHORT).show()
        }
    }

    return launchCameraWithNewPhotoUri
}

/**
 * 사진 파일을 생성하고, 해당 파일의 URI를 반환
 */
fun createPhotoUri(context: Context): Uri? {
    return try {
        val photoFile = createImageFile(context)
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }
}

/**
 * 앱 내부 캐시 디렉토리에 JPEG 형식의 임시 파일 생성
 */
fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir: File? = context.cacheDir
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
}