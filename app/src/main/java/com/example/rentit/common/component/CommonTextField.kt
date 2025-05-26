package com.example.rentit.common.component

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    minLines: Int = 1,
    maxLines: Int = 1,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    placeholderAlignment: Alignment = Alignment.CenterStart,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    var borderColor by remember { mutableStateOf(Gray200) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))  // 20dp 반경을 가진 둥근 모서리
            .onFocusChanged {
                borderColor = if(it.isFocused) PrimaryBlue500 else Gray200
            },
        textStyle = textStyle,  // 텍스트 스타일
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
                    .basicRoundedGrayBorder(color = borderColor)
                    .padding(20.dp, 12.dp),
                contentAlignment = placeholderAlignment
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray400
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
        CommonTextField(
            value = "",
            onValueChange = { },
            placeholder = "Place Holder",
        )
    }
}
