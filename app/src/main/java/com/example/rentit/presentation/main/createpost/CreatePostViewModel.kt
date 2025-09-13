package com.example.rentit.presentation.main.createpost

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.util.MultipartUtil
import com.example.rentit.domain.product.usecase.CreatePostUseCase
import com.example.rentit.domain.product.usecase.GetCategoryMapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

private const val TAG = "CreatePostViewModel"
private const val MAX_CATEGORY_COUNT = 3

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val getCategoryMapUseCase: GetCategoryMapUseCase,
    private val createPostUseCase: CreatePostUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostState())
    val uiState: StateFlow<CreatePostState> = _uiState

    private val _sideEffect = MutableSharedFlow<CreatePostSideEffect>()
    val sideEffect: SharedFlow<CreatePostSideEffect> = _sideEffect

    init {
        getCategoryList()
    }

    private fun updateState(transform: CreatePostState.() -> CreatePostState) {
        _uiState.value = _uiState.value.transform()
    }

    private fun emitSideEffect(effect: CreatePostSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }

    private fun getCategoryList() {
        viewModelScope.launch {
            getCategoryMapUseCase()
                .onSuccess {
                    Log.i(TAG, "카테고리 조회 성공: ${it.size}개")
                    updateState { copy(categoryMap = it) }
                }.onFailure { e ->
                    Log.e(TAG, "카테고리 조회 실패", e)
                }
        }
    }

    fun updateTitle(title: String) {
        updateState {
            copy(title = title, showEmptyTitleError = false)
        }
    }

    fun updateContent(content: String) {
        updateState {
            copy(content = content, showEmptyContentError = false)
        }
    }

    fun updateImageUriList(uriList: List<Uri>) {
        updateState { copy(selectedImgUriList = _uiState.value.selectedImgUriList + uriList) }
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

        if(newSelected.size <= MAX_CATEGORY_COUNT) {
            updateState { copy(selectedCategoryIdList = newSelected) }
        }
    }

    fun removeSelectedCategory(categoryId: Int) {
        updateState { copy(selectedCategoryIdList = _uiState.value.selectedCategoryIdList - categoryId) }
    }

    fun updatePeriod(position: ClosedFloatingPointRange<Float>) {
        updateState { copy(periodSliderPosition = position) }
    }

    fun updatePrice(price: TextFieldValue) {
        updateState {
            copy(priceTextFieldValue = price, showEmptyPriceError = false)
        }
    }

    private fun checkValid(): Boolean {
        val title = _uiState.value.title
        val content = _uiState.value.content
        val price = _uiState.value.price

        if(title.isNotBlank() && content.isNotBlank() && price > 0) {
            return true
        }

        updateState {
            copy(
                showEmptyTitleError = title.isBlank(),
                showEmptyContentError = content.isBlank(),
                showEmptyPriceError = price <= 0
            )
        }
        return false
    }

    private fun createPostErrorHandling(e: Throwable) {
        emitSideEffect(
            when(e) {
                is IOException -> CreatePostSideEffect.ShowNetworkErrorToast
                else -> CreatePostSideEffect.ShowPostErrorToast
            }
        )
    }

    fun createPost(context: Context) {
        val isValid = checkValid()

        if(!isValid) {
            emitSideEffect(CreatePostSideEffect.ShowInvalidInputToast)
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            val selectedImageUriList = _uiState.value.selectedImgUriList
            val imageParts =
                if (selectedImageUriList.isNotEmpty()) {
                    selectedImageUriList.mapNotNull {
                        MultipartUtil.uriToMultipart(context, it)
                    }
                } else null

            createPostUseCase(
                imageParts = imageParts,
                title = _uiState.value.title,
                content = _uiState.value.content,
                selectedCategoryIdList = _uiState.value.selectedCategoryIdList,
                periodSliderPosition = _uiState.value.periodSliderPosition,
                price = _uiState.value.price,
            ).onSuccess {
                val productId = it.productId
                Log.i(TAG, "게시글 생성 성공: Product Id: $productId")
                emitSideEffect(CreatePostSideEffect.NavigateToProductDetail(productId))
            }.onFailure { e ->
                Log.e(TAG, "게시글 생성 실패", e)
                createPostErrorHandling(e)
            }
            updateState { copy(isLoading = false) }
        }
    }
}