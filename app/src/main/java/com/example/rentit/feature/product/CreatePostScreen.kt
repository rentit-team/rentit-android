package com.example.rentit.feature.product

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonTextField
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.basicRoundedGrayBorder
import com.example.rentit.common.component.moveScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.product.dto.CategoryDto
import com.example.rentit.data.product.dto.CreatePostRequestDto
import com.example.rentit.data.product.dto.PeriodDto
import com.example.rentit.feature.product.component.RemovableTagButton
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(navHostController: NavHostController) {
    var titleText by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }
    var showTagDrawer by remember { mutableStateOf(false) }
    var periodSliderPosition by remember { mutableStateOf(3F..15F) }
    var price by remember { mutableIntStateOf(0) }
    var priceValue by remember { mutableStateOf("") }
    val createPostViewModel: CreatePostViewModel = hiltViewModel()
    val selectedCategoryList by createPostViewModel.categoryTagList.collectAsStateWithLifecycle()
    val selectedImgUriList by createPostViewModel.selectedImgUriList.collectAsStateWithLifecycle()

    val priceLimit = 5000000

    Scaffold(
        topBar = { CommonTopAppBar(onClick = { /*TODO*/ }) }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .screenHorizontalPadding()
                .verticalScroll(state = rememberScrollState())
        ) {
            LabeledContent(stringResource(id = R.string.screen_product_create_image_label)) {
                ImageSelectSection(
                    selectedImgUriList = selectedImgUriList,
                    onUpdateImageList = createPostViewModel::updateImageUriList,
                    onImageRemoveClick = createPostViewModel::removeImageUri
                )
            }
            LabeledContent(stringResource(id = R.string.screen_product_create_title_label)) {
                CommonTextField(
                    value = titleText,
                    onValueChange = { value -> titleText = value },
                    placeholder = stringResource(id = R.string.screen_product_create_title_placeholder))
            }
            LabeledContent(stringResource(id = R.string.screen_product_create_content_label)){
                CommonTextField(
                    value = contentText,
                    onValueChange = { value -> contentText = value },
                    placeholder = stringResource(id = R.string.screen_product_create_content_placeholder),
                    minLines = 4,
                    maxLines = Int.MAX_VALUE,
                    isSingleLine = false,
                    imeAction = ImeAction.Default,
                    placeholderAlignment = Alignment.TopStart
                )
            }
            LabeledContent(stringResource(id = R.string.screen_product_create_category_label)) {
                CategoryTagSection(
                    selectedCategoryList,
                    onRemoveCategory = createPostViewModel::removeSelectedCategory,
                    onAddCategory = { showTagDrawer = true }
                )
            }
            LabeledContent(stringResource(id = R.string.screen_product_create_rental_period_label)){
                RentalPeriodSlider(periodSliderPosition) { pos -> periodSliderPosition = pos }
            }
            LabeledContent(stringResource(id = R.string.screen_product_create_price_label)){
                PriceInputSection(priceValue) {value ->
                    val value = value.replace(",", "")
                    val df = DecimalFormat("#,###,###")
                    if (value.isNotEmpty()) {
                        price = value.toInt()
                        if (price > priceLimit) {
                            price = priceLimit
                        }
                        priceValue = df.format(price)
                    } else {
                        price = 0
                        priceValue = ""

                    }
                }
            }
            CommonButton(
                text = stringResource(id = R.string.screen_product_create_complete_btn_text),
                containerColor = PrimaryBlue500,
                contentColor = Color.White,
                modifier = Modifier.padding(top = 30.dp, bottom = 50.dp)
            ) {
                val period = PeriodDto("daily", periodSliderPosition.start.toInt(), periodSliderPosition.endInclusive.toInt())
                val selectedCategoryIdList = selectedCategoryList.map { cat -> cat.id }
                val requestBody = CreatePostRequestDto(titleText, contentText, selectedCategoryIdList, period, price.toDouble(), null)
                val thumbnailImg = if(selectedImgUriList.isNotEmpty()) selectedImgUriList[0] else null
                createPostViewModel.createPost(requestBody, thumbnailImg)
            }
        }
        if(showTagDrawer) {
            ModalBottomSheet(onDismissRequest = { showTagDrawer = false }) {
                CategoryTagDrawer(
                    selectedCategoryList = selectedCategoryList,
                    onTagButtonClick = createPostViewModel::handleCategoryClick
                )
            }
        }
    }
    CreatePostResultHandler(createPostViewModel) {
        moveScreen(navHostController, NavigationRoutes.MAIN)
    }
}

@Composable
fun LabeledContent(title: String, content: @Composable () -> Unit) {
        Text(
            modifier = Modifier.padding(bottom = 15.dp, top = 20.dp),
            text = title,
            style = MaterialTheme.typography.bodyLarge)
        content()
}

@Composable
fun ImageSelectSection(
    selectedImgUriList: List<Uri>,
    onUpdateImageList: (List<Uri>) -> Unit,
    onImageRemoveClick: (Uri) -> Unit
) {
    val pickMultipleImage = PickMultipleImage(onUpdateImageList)
    val boxModifier = Modifier
        .width(160.dp)
        .height(120.dp)
        .padding(end = 12.dp)
        .basicRoundedGrayBorder()
    Row(Modifier.horizontalScroll(state = rememberScrollState())) {
        selectedImgUriList.forEach { uri ->
            Box(modifier = boxModifier.clip(RoundedCornerShape(20.dp))) {
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = uri,
                    contentDescription = stringResource(id = R.string.screen_product_create_selected_image_description),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .size(20.dp)
                        .background(Color(255, 255, 255, 160)),
                    onClick = { onImageRemoveClick(uri) }
                ) {
                    Icon(
                        modifier = Modifier.size(8.dp),
                        painter = painterResource(id = R.drawable.ic_x_bold),
                        tint = AppBlack,
                        contentDescription = stringResource(id = R.string.screen_product_create_category_add_text)
                    )
                }
            }
        }
        Box(modifier = boxModifier.clickable {
            pickMultipleImage.launch(
                PickVisualMediaRequest(
                    PickVisualMedia.ImageOnly
                )
            )
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.img_image),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.screen_product_create_add_image_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray400,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PickMultipleImage(onUpdateImageList: (List<Uri>) -> Unit): ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>> {
    return rememberLauncherForActivityResult(
        PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            Log.d("PhotoPicker", "Number of item selected: ${uris.size}")
            uris.forEach { uri ->
                Log.d("PhotoPicker", "Selected: $uri")
            }
            onUpdateImageList(uris)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryTagSection(
    selectedCategoryList: List<CategoryDto>,
    onRemoveCategory: (CategoryDto) -> Unit,
    onAddCategory: () -> Unit) {
    FlowRow(
        modifier = Modifier.padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        selectedCategoryList.forEach { cat ->
            RemovableTagButton(text = cat.name) {
                onRemoveCategory(cat)
            }
        }
        AddCategoryButton(onAddCategory)
    }
}
@Composable
fun AddCategoryButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier
            .height(36.dp),
        onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(id = R.drawable.ic_plus),
                tint = PrimaryBlue500,
                contentDescription = stringResource(id = R.string.screen_product_create_category_add_text)
            )
            Text(modifier = Modifier.padding(start = 6.dp), text = stringResource(id = R.string.screen_product_create_category_add_text), color = PrimaryBlue500)
        }
    }
}
@Composable
fun RentalPeriodSlider(sliderPosition: ClosedFloatingPointRange<Float>, onValueChange: (ClosedFloatingPointRange<Float>) -> Unit) {
    Column {
        Text(
            text = stringResource(
                id = R.string.screen_product_create_rental_period_text,
                sliderPosition.start.toInt(),
                sliderPosition.endInclusive.toInt()
            )
        )
        RangeSlider(
            value = sliderPosition,
            steps = 30,
            onValueChange = { onValueChange(it) },
            valueRange = 1F..30F,
            onValueChangeFinished = {},
            colors = SliderDefaults.colors(
                // 핸들 색상
                thumbColor = PrimaryBlue500,

                // 트랙 색상
                activeTrackColor = PrimaryBlue500,
                inactiveTrackColor = Gray200,

                // 눈금 색상
                activeTickColor = PrimaryBlue500,
                inactiveTickColor = Gray200
            )
        )
    }
}
@Composable
fun PriceInputSection(priceValue: String, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CommonTextField(
            value = priceValue,
            onValueChange = onValueChange,
            placeholder = "0",
            keyboardType = KeyboardType.Number,
            textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.End),
            placeholderAlignment = Alignment.CenterEnd,
            modifier = Modifier.width(140.dp)
        )
        Text(
            text = stringResource(id = R.string.screen_product_create_price_unit),
            modifier = Modifier.padding(start = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Gray800
        )
    }
}

@Composable
fun CreatePostResultHandler(createPostViewModel: CreatePostViewModel, onCreatePostSuccess: () -> Unit){
    val createPostResult by createPostViewModel.createPostResult.collectAsStateWithLifecycle()
    LaunchedEffect(createPostResult) {
        createPostResult?.onSuccess {
            onCreatePostSuccess()
        }?.onFailure {
            /* 게시글 생성 실패 시 */
        }
    }
}

@Preview
@Composable
fun PreviewProductCreateScreen() {
    RentItTheme {
        CreatePostScreen(rememberNavController())
    }
}