package com.example.rentit.presentation.main.createpost

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.CategoryDto
import com.example.rentit.data.product.dto.CreatePostRequestDto
import com.example.rentit.domain.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostState())
    val uiState: StateFlow<CreatePostState> = _uiState

    init {
        getCategoryList()
    }

    private fun updateState(transform: CreatePostState.() -> CreatePostState) {
        _uiState.value = _uiState.value.transform()
    }

    private fun getCategoryList() {
        viewModelScope.launch {
            repository.getCategories().onSuccess {
                _uiState.value = _uiState.value.copy(categoryList = it.categories)
            }.onFailure {
                /* 카테고리 로딩 실패 시 */
            }
        }
    }

    fun updateTitle(title: String) {
        updateState { copy(title = title) }
    }

    fun updateContent(content: String) {
        updateState { copy(content = content) }
    }

    fun updateImageUriList(uriList: List<Uri>) {
        updateState { copy(selectedImgUriList = uriList) }
    }

    fun removeImageUri(uri: Uri){
        updateState { copy(selectedImgUriList = _uiState.value.selectedImgUriList - uri) }
    }

    fun showCategoryTagDrawer() {
        updateState { copy(showCategoryTagDrawer = true) }
    }

    fun hideCategoryTagDrawer() {
        updateState { copy(showCategoryTagDrawer = false) }
    }

    fun handleCategoryClick(category: CategoryDto) {
        val currentSelected = _uiState.value.selectedCategoryList
        val newSelected = if(currentSelected.contains(category)){
            currentSelected - category
        } else {
            currentSelected + category
        }
        updateState { copy(selectedCategoryList = newSelected) }
    }

    fun removeSelectedCategory(category: CategoryDto) {
        updateState { copy(selectedCategoryList = _uiState.value.selectedCategoryList - category) }
    }

    fun updatePeriod(position: ClosedFloatingPointRange<Float>) {
        updateState { copy(periodSliderPosition = position) }
    }

    fun updatePrice(price: TextFieldValue) {
        updateState { copy(priceTextFieldValue = price) }
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

    private fun uriToFile(uri: Uri): File? {
        try {
            val inputStream = applicationContext.contentResolver.openInputStream(uri)
            val fileName = "IMG_${System.currentTimeMillis()}.jpg"
            val tempFile = File(applicationContext.cacheDir, fileName)

            inputStream?.use { input ->
                FileOutputStream(tempFile).use { input.copyTo(it) }
            }
            return tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}