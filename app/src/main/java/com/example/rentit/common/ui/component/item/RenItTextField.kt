package com.example.rentit.common.ui.component.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.ui.extension.rentItBasicRoundedGrayBorder
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme


private object CommonTextFieldDefaults {
    val BorderRadius = 20.dp
    val InnerPaddingValue = PaddingValues(20.dp, 12.dp)
    val BorderColorFocused = PrimaryBlue500
    val BorderColorDefault = Gray200
    val PlaceHolderTextColor = Gray400
}

@Composable
fun RentItTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    minLines: Int = 1,
    maxLines: Int = 1,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    placeholderAlignment: Alignment = Alignment.CenterStart,
) {
    var borderColor by remember { mutableStateOf(CommonTextFieldDefaults.BorderColorDefault) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CommonTextFieldDefaults.BorderRadius))
            .onFocusChanged {
                borderColor = if (it.isFocused) CommonTextFieldDefaults.BorderColorFocused else CommonTextFieldDefaults.BorderColorDefault
            },
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction  // 키보드 오른쪽 하단 버튼 설정
        ),
        minLines = minLines,
        maxLines = maxLines,
        singleLine = isSingleLine,
        decorationBox = { innerTextField ->
            // 텍스트 필드 테두리와 배경 설정
            Box(
                modifier = Modifier
                    .rentItBasicRoundedGrayBorder(color = borderColor)
                    .padding(CommonTextFieldDefaults.InnerPaddingValue),
                contentAlignment = placeholderAlignment
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CommonTextFieldDefaults.PlaceHolderTextColor
                    )
                }
                innerTextField()  // 실제 입력 텍스트를 보여주는 부분
            }
        }
    )
}


@Composable
fun RentItTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    enabled: Boolean = true,
    placeholder: String = "",
    minLines: Int = 1,
    maxLines: Int = 1,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    placeholderAlignment: Alignment = Alignment.CenterStart,
) {
    var borderColor by remember { mutableStateOf(CommonTextFieldDefaults.BorderColorDefault) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CommonTextFieldDefaults.BorderRadius))
            .onFocusChanged {
                borderColor = if (it.isFocused) CommonTextFieldDefaults.BorderColorFocused else CommonTextFieldDefaults.BorderColorDefault
            },
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction  // 키보드 오른쪽 하단 버튼 설정
        ),
        minLines = minLines,
        maxLines = maxLines,
        singleLine = isSingleLine,
        decorationBox = { innerTextField ->
            // 텍스트 필드 테두리와 배경 설정
            Box(
                modifier = Modifier
                    .rentItBasicRoundedGrayBorder(color = borderColor)
                    .padding(CommonTextFieldDefaults.InnerPaddingValue),
                contentAlignment = placeholderAlignment
            ) {
                if (value.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CommonTextFieldDefaults.PlaceHolderTextColor
                    )
                }
                innerTextField()  // 실제 입력 텍스트를 보여주는 부분
            }
        }
    )
}

@Preview
@Composable
fun PreviewBaseTextField() {
    RentItTheme {
        RentItTextField(
            value = "",
            onValueChange = { },
            placeholder = "Place Holder",
        )
    }
}