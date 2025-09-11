package com.example.rentit.presentation.main.createpost

import android.content.Context
import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.product.usecase.CreatePostUseCase
import com.example.rentit.domain.product.usecase.GetCategoryMapUseCase
import com.example.rentit.presentation.main.createpost.components.createMultipartFromUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val getCategoryMapUseCase: GetCategoryMapUseCase,
    private val createPostUseCase: CreatePostUseCase,
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
            getCategoryMapUseCase()
                .onSuccess {
                    updateState { copy(categoryMap = it) }
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

    fun handleCategoryClick(categoryId: Int) {
        val currentSelected = _uiState.value.selectedCategoryIdList
        val newSelected = if(currentSelected.contains(categoryId)){
            currentSelected - categoryId
        } else {
            currentSelected + categoryId
        }
        updateState { copy(selectedCategoryIdList = newSelected) }
    }

    fun removeSelectedCategory(categoryId: Int) {
        updateState { copy(selectedCategoryIdList = _uiState.value.selectedCategoryIdList - categoryId) }
    }

    fun updatePeriod(position: ClosedFloatingPointRange<Float>) {
        updateState { copy(periodSliderPosition = position) }
    }

    fun updatePrice(price: TextFieldValue) {
        updateState { copy(priceTextFieldValue = price) }
    }

    fun createPost(context: Context) {
        viewModelScope.launch {
            // 서버는 단일 이미지만 지원하므로 첫 번째 선택 이미지를 업로드 대상으로 사용
            val selectedImageUriList = _uiState.value.selectedImgUriList
            val thumbnailPart = if (selectedImageUriList.isNotEmpty()) createMultipartFromUri(
                context = context,
                uri = selectedImageUriList[0]
            ) else null

            createPostUseCase(
                thumbnailPart = thumbnailPart,
                title = _uiState.value.title,
                content = _uiState.value.content,
                selectedCategoryIdList = _uiState.value.selectedCategoryIdList,
                periodSliderPosition = _uiState.value.periodSliderPosition,
                price = _uiState.value.price,
            )
        }
    }
}