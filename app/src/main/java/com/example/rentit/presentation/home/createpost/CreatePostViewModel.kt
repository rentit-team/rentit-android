package com.example.rentit.presentation.home.createpost

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.CategoryDto
import com.example.rentit.data.product.dto.CreatePostRequestDto
import com.example.rentit.data.product.dto.CreatePostResponseDto
import com.example.rentit.data.product.repository.ProductRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val repository: ProductRepository
) : ViewModel() {

    private val _categoryList = MutableStateFlow<List<CategoryDto>>(emptyList())
    val categoryList: StateFlow<List<CategoryDto>> = _categoryList

    private val _selectedCategoryList =  MutableStateFlow<List<CategoryDto>>(emptyList())
    val selectedCategoryList: StateFlow<List<CategoryDto>> = _selectedCategoryList

    private val _selectedImgUriList =  MutableStateFlow<List<Uri>>(emptyList())
    val selectedImgUriList: StateFlow<List<Uri>> = _selectedImgUriList

    private val _createPostResult =  MutableStateFlow<Result<CreatePostResponseDto>?>(null)
    val createPostResult: StateFlow<Result<CreatePostResponseDto>?> = _createPostResult

    init {
        getCategoryList()
    }

    private fun getCategoryList() {
        viewModelScope.launch {
            repository.getCategories().onSuccess {
                _categoryList.value = it.categories
            }.onFailure {
                /* 카테고리 로딩 실패 시 */
            }
        }
    }

    fun handleCategoryClick(category: CategoryDto) {
        if(_selectedCategoryList.value.contains(category)){
            _selectedCategoryList.value -= category
        } else {
            _selectedCategoryList.value += category
        }
    }

    fun removeSelectedCategory(category: CategoryDto) {
        if(_selectedCategoryList.value.contains(category)){
            _selectedCategoryList.value -= category
        }
    }

    fun updateImageUriList(uriList: List<Uri>) {
        _selectedImgUriList.value = uriList
    }

    fun removeImageUri(uri: Uri){
        _selectedImgUriList.value -= uri
    }

    fun createPost(postData: CreatePostRequestDto, thumbnailImgUri: Uri?) {
        val gson = Gson()
        var thumbnailImg: MultipartBody.Part? = null
        if(thumbnailImgUri != null) thumbnailImg = createMultipartFromUri(thumbnailImgUri)
        val payloadJson = gson.toJson(postData)
        val payloadRequestBody = payloadJson.toRequestBody("application/json".toMediaTypeOrNull())
        viewModelScope.launch {
            _createPostResult.value = repository.createPost(payloadRequestBody, thumbnailImg)
        }
    }

    private fun uriToFile(uri: Uri): File? {
        return try{
            val inputStream = applicationContext.contentResolver.openInputStream(uri)
            val fileName = getFileName(uri)
            val tempFile = File(applicationContext.cacheDir, fileName)

            inputStream?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileName(uri: Uri): String {
        var fileName = "temp_file"
        val cursor = applicationContext.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if(it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                if(nameIndex >= 0) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }

    private fun createMultipartFromUri(uri: Uri): MultipartBody.Part? {
        val file = uriToFile(uri) ?: return null

        val mimeType = applicationContext.contentResolver.getType(uri) ?: "image/*"
        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(
            "thumbnailImg",
            file.name,
            requestBody
        )
    }
}