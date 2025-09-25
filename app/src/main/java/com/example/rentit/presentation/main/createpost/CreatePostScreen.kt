package com.example.rentit.presentation.main.createpost

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.ui.component.item.RentItBasicButton
import com.example.rentit.common.ui.component.item.RentItTextField
import com.example.rentit.common.ui.component.layout.RentItTopAppBar
import com.example.rentit.common.ui.component.item.RentItInputErrorMessage
import com.example.rentit.common.ui.extension.rentItBasicRoundedGrayBorder
import com.example.rentit.common.ui.component.item.RentItRemovableImageBox
import com.example.rentit.common.ui.extension.rentItScreenHorizontalPadding
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.ui.formatter.priceFormatter
import com.example.rentit.domain.product.model.CategoryModel
import com.example.rentit.presentation.main.createpost.drawer.CategoryTagDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    title: String,
    content: String,
    selectedImgUriList: List<Uri>,
    categoryMap: Map<Int, CategoryModel>,
    selectedCategoryList: List<Int>,
    periodSliderPosition: ClosedFloatingPointRange<Float>,
    price: Int,
    showEmptyTitleError: Boolean,
    showEmptyContentError: Boolean,
    showEmptyPriceError: Boolean,
    showCategoryTagDrawer: Boolean,
    isPostButtonAvailable: Boolean,
    onBackClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onCategoryClick: (Int) -> Unit,
    onAddImageBoxClick: () -> Unit,
    onImageRemoveClick: (Uri) -> Unit,
    onAddCategoryClick: () -> Unit,
    onRemoveCategory: (Int) -> Unit,
    onCategoryDialogDismiss: () -> Unit,
    onPeriodChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onPriceChange: (TextFieldValue) -> Unit,
    onPostClick: () -> Unit
) {
    Scaffold(
        topBar = { RentItTopAppBar(onBackClick = onBackClick) }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .rentItScreenHorizontalPadding()
                .verticalScroll(state = rememberScrollState())
        ) {
            ImageSelectSection(
                selectedImgUriList = selectedImgUriList,
                onAddImageBoxClick = onAddImageBoxClick,
                onImageRemoveClick = onImageRemoveClick
            )

            TitleSection(
                title = title,
                showEmptyTitleError = showEmptyTitleError,
                onTitleChange = onTitleChange
            )

            ContentSection(
                content = content,
                showEmptyContentError = showEmptyContentError,
                onContentChange = onContentChange
            )


            CategoryTagSection(
                categoryMap = categoryMap,
                selectedCategoryList = selectedCategoryList,
                onRemoveCategory = onRemoveCategory,
                onAddCategoryClick = onAddCategoryClick
            )

            RentalPeriodSection(periodSliderPosition, onPeriodChange)

            PriceInputSection(
                price = price,
                showEmptyPriceError = showEmptyPriceError,
                onPriceChange = onPriceChange
            )

            RentItBasicButton(
                text = stringResource(id = R.string.screen_product_create_complete_btn_text),
                containerColor = PrimaryBlue500,
                contentColor = Color.White,
                modifier = Modifier.padding(top = 30.dp, bottom = 50.dp),
                enabled = isPostButtonAvailable,
                onClick = onPostClick
            )
        }

        if(showCategoryTagDrawer) {
            ModalBottomSheet(onDismissRequest = onCategoryDialogDismiss) {
                CategoryTagDrawer(
                    categoryMap = categoryMap,
                    selectedCategoryList = selectedCategoryList,
                    onTagButtonClick = onCategoryClick
                )
            }
        }
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
    onAddImageBoxClick: () -> Unit = {},
    onImageRemoveClick: (Uri) -> Unit
) {
    val imageBoxWidth = 160.dp
    val imageBoxAspectRatio = 4f/3f

    LabeledContent(stringResource(id = R.string.screen_product_create_image_label)) {
        Row(Modifier.horizontalScroll(state = rememberScrollState())) {
            selectedImgUriList.forEach { uri ->
                RentItRemovableImageBox(imageBoxWidth, imageBoxAspectRatio, uri, onImageRemoveClick)
                Spacer(Modifier.width(10.dp))
            }
            Box(
                modifier = Modifier
                    .width(imageBoxWidth)
                    .aspectRatio(imageBoxAspectRatio)
                    .rentItBasicRoundedGrayBorder()
                    .clickable { onAddImageBoxClick() }) {
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
}

@Composable
fun TitleSection(title: String, showEmptyTitleError: Boolean, onTitleChange: (String) -> Unit) {
    LabeledContent(stringResource(id = R.string.screen_product_create_title_label)) {
        Column {
            RentItTextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = stringResource(id = R.string.screen_product_create_title_placeholder)
            )
            if(showEmptyTitleError){
                RentItInputErrorMessage(
                    text = stringResource(R.string.screen_product_create_empty_error_title)
                )
            }
        }
    }
}

@Composable
fun ContentSection(content: String, showEmptyContentError: Boolean, onContentChange: (String) -> Unit) {
    LabeledContent(stringResource(id = R.string.screen_product_create_content_label)){
        Column {
            RentItTextField(
                value = content,
                onValueChange = onContentChange,
                placeholder = stringResource(id = R.string.screen_product_create_content_placeholder),
                minLines = 4,
                maxLines = Int.MAX_VALUE,
                isSingleLine = false,
                imeAction = ImeAction.Default,
                placeholderAlignment = Alignment.TopStart
            )
            if(showEmptyContentError){
                RentItInputErrorMessage(
                    text = stringResource(R.string.screen_product_create_empty_error_content)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryTagSection(
    categoryMap: Map<Int, CategoryModel>,
    selectedCategoryList: List<Int>,
    onRemoveCategory: (Int) -> Unit,
    onAddCategoryClick: () -> Unit
) {
    LabeledContent(stringResource(id = R.string.screen_product_create_category_label)) {
        FlowRow(
            modifier = Modifier.padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedCategoryList.forEach { categoryId ->
                categoryMap[categoryId]?.let {
                    RemovableTagButton(text = it.name) {
                        onRemoveCategory(categoryId)
                    }
                }
            }
            AddCategoryButton(onAddCategoryClick)
        }
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
fun RemovableTagButton(text: String, onRemoveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(25.dp))
            .rentItBasicRoundedGrayBorder(color = PrimaryBlue500)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 6.dp),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = PrimaryBlue500
        )
        IconButton(modifier = Modifier.width(12.dp), onClick = onRemoveClick) {
            Icon(
                modifier = Modifier.width(10.dp),
                painter = painterResource(id = R.drawable.ic_x),
                contentDescription = stringResource(id = R.string.common_removable_tag_btn_icon_delete_description),
                tint = PrimaryBlue500
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RentalPeriodSection(sliderPosition: ClosedFloatingPointRange<Float>, onValueChange: (ClosedFloatingPointRange<Float>) -> Unit) {
    LabeledContent(stringResource(id = R.string.screen_product_create_rental_period_label)) {
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
}

@Composable
fun PriceInputSection(price: Int, showEmptyPriceError: Boolean, onPriceChange: (TextFieldValue) -> Unit) {
    val formattedPrice = priceFormatter(price)

    LabeledContent(stringResource(id = R.string.screen_product_create_price_label)) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RentItTextField(
                    value = TextFieldValue(
                        text = formattedPrice,
                        selection = TextRange(formattedPrice.length)    // 커서를 항상 맨 뒤로 이동
                    ),
                    onValueChange = onPriceChange,
                    placeholder = "0",
                    keyboardType = KeyboardType.Number,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.End),
                    placeholderAlignment = Alignment.CenterEnd,
                    modifier = Modifier.width(140.dp)
                )
                Text(
                    text = stringResource(id = R.string.common_price_unit_per_day),
                    modifier = Modifier.padding(start = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray800
                )
            }
            if(showEmptyPriceError){
                RentItInputErrorMessage(
                    text = stringResource(R.string.screen_product_create_empty_error_price)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewProductCreateScreen() {
    RentItTheme {
        CreatePostScreen(
            title = "",
            content = "",
            selectedImgUriList = emptyList(),
            categoryMap = emptyMap(),
            selectedCategoryList = emptyList(),
            periodSliderPosition = 3F..15F,
            price = 0,
            showEmptyTitleError = false,
            showEmptyContentError = false,
            showEmptyPriceError = false,
            showCategoryTagDrawer = false,
            isPostButtonAvailable = false,
            onBackClick = {},
            onTitleChange = {},
            onContentChange = {},
            onCategoryClick = {},
            onImageRemoveClick = {},
            onAddCategoryClick = {},
            onAddImageBoxClick = {},
            onRemoveCategory = {},
            onCategoryDialogDismiss = {},
            onPeriodChange = {},
            onPriceChange = {},
            onPostClick = {}
        )
    }
}