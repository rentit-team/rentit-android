package com.example.rentit.feature.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonTextField
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.RentItTheme

@Composable
fun ProductCreateScreen() {
    var titleText by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }
    Scaffold(
        topBar = { CommonTopAppBar(onClick = { /*TODO*/ }) }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .screenHorizontalPadding()
                .verticalScroll(state = rememberScrollState())) {
            LabeledContent(stringResource(id = R.string.screen_product_create_title_label)){
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
                    imeAction = ImeAction.Default)
            }
            LabeledContent(stringResource(id = R.string.screen_product_create_category_label)){
            }
            LabeledContent(stringResource(id = R.string.screen_product_create_rental_period_label)){
            }
            LabeledContent(stringResource(id = R.string.screen_product_create_price_label)){
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

@Preview
@Composable
fun PreviewProductCreateScreen() {
    RentItTheme {
        ProductCreateScreen()
    }
}