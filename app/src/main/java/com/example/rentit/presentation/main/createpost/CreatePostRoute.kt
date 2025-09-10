package com.example.rentit.presentation.main.createpost


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

@Composable
fun CreatePostRoute(navHostController: NavHostController) {
    val viewModel: CreatePostViewModel = hiltViewModel()
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

    CreatePostScreen(
        title = uiState.title,
        content = uiState.content,
        selectedImgUriList = uiState.selectedImgUriList,
        categoryList = uiState.categoryList,
        selectedCategoryList = uiState.selectedCategoryList,
        periodSliderPosition = uiState.periodSliderPosition,
        price = uiState.price,
        showCategoryTagDrawer = uiState.showCategoryTagDrawer,
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
        onPostClick = { },
    )
}