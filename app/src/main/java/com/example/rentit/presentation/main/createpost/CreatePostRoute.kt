package com.example.rentit.presentation.main.createpost

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.navigation.productdetail.navigateToProductDetailFromCreate

@Composable
fun CreatePostRoute(navHostController: NavHostController) {
    val viewModel: CreatePostViewModel = hiltViewModel()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pickMultipleImageLauncher =
        rememberLauncherForActivityResult(PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                viewModel.updateImageUriList(uris)
            }
        }

    val launchImagePicker = {
        pickMultipleImageLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect {
                when (it) {
                    is CreatePostSideEffect.NavigateToProductDetail -> {
                        navHostController.navigateToProductDetailFromCreate(it.productId)
                    }
                    CreatePostSideEffect.ShowInvalidInputToast -> {
                        Toast.makeText(context, R.string.toast_post_invalid_input, Toast.LENGTH_SHORT).show()
                    }
                    CreatePostSideEffect.ShowNetworkErrorToast -> {
                        Toast.makeText(context, R.string.toast_post_network_error, Toast.LENGTH_SHORT).show()
                    }
                    CreatePostSideEffect.ShowPostErrorToast -> {
                        Toast.makeText(context, R.string.toast_post_server_error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    CreatePostScreen(
        title = uiState.title,
        content = uiState.content,
        selectedImgUriList = uiState.selectedImgUriList,
        categoryMap = uiState.categoryMap,
        selectedCategoryList = uiState.selectedCategoryIdList,
        periodSliderPosition = uiState.periodSliderPosition,
        price = uiState.price,
        showEmptyTitleError = uiState.showEmptyTitleError,
        showEmptyContentError = uiState.showEmptyContentError,
        showEmptyPriceError = uiState.showEmptyPriceError,
        showCategoryTagDrawer = uiState.showCategoryTagDrawer,
        isPostButtonAvailable = uiState.isPostButtonAvailable,
        onBackClick = navHostController::popBackStack,
        onTitleChange = viewModel::updateTitle,
        onContentChange = viewModel::updateContent,
        onCategoryClick = viewModel::handleCategoryClick,
        onAddImageBoxClick = launchImagePicker,
        onImageRemoveClick = viewModel::removeImageUri,
        onAddCategoryClick = viewModel::showCategoryTagDrawer,
        onRemoveCategory = viewModel::removeSelectedCategory,
        onCategoryDialogDismiss = viewModel::hideCategoryTagDrawer,
        onPeriodChange = viewModel::updatePeriod,
        onPriceChange = viewModel::updatePrice,
        onPostClick = { viewModel.createPost(context) },
    )

    LoadingScreen(uiState.isLoading)
}