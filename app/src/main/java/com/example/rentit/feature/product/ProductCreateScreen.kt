package com.example.rentit.feature.product

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonTagButton
import com.example.rentit.common.component.CommonTextField
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import java.text.DecimalFormat

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
                .verticalScroll(state = rememberScrollState())
        ) {
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
                    imeAction = ImeAction.Default,
                    placeholderAlignment = Alignment.TopStart
                )
            }
            LabeledContent(stringResource(id = R.string.screen_product_create_category_label)){
                CommonTagButton("Tag") { Log.d("TAG", "CLICKED") }
            }
            LabeledContent(stringResource(id = R.string.screen_product_create_rental_period_label)){
                PriceRangeSlider()
            }
            LabeledContent(stringResource(id = R.string.screen_product_create_price_label)){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PriceTextField()
                    Text(
                        text = stringResource(id = R.string.screen_product_create_price_unit),
                        modifier = Modifier.padding(start = 10.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray800
                    )
                }
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
fun PriceRangeSlider() {
    var sliderPosition by remember { mutableStateOf(3F..15F) }
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
            onValueChange = { sliderPosition = it },
            valueRange = 1F..30F,
            onValueChangeFinished = {},
            colors = SliderDefaults.colors(
                // 핸들 색상
                thumbColor = PrimaryBlue500,

                // 트랙 색상
                activeTrackColor = PrimaryBlue500,
                inactiveTrackColor = Gray100,

                // 눈금 색상
                activeTickColor = PrimaryBlue500,
                inactiveTickColor = Gray100
            )
        )
    }
}
@Composable
fun PriceTextField() {
    val priceLimit = 5000000
    var price by remember { mutableIntStateOf(0) }
    var priceValue by remember { mutableStateOf("") }

    CommonTextField(
        value = priceValue,
        onValueChange = {
            val value = it.replace(",", "")
            val df = DecimalFormat("#,###,###")
            if(value.isNotEmpty()){
                price = value.toInt()
                if(price > priceLimit){
                    price = priceLimit
                }
                priceValue = df.format(price)
            } else {
                price = 0
                priceValue = ""
            }},
        placeholder = "0",
        keyboardType = KeyboardType.Number,
        textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.End),
        placeholderAlignment = Alignment.CenterEnd,
        modifier = Modifier.width(140.dp)
    )
}

@Preview
@Composable
fun PreviewProductCreateScreen() {
    RentItTheme {
        ProductCreateScreen()
    }
}