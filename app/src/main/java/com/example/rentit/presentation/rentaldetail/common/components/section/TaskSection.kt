package com.example.rentit.presentation.rentaldetail.common.components.section

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.rentaldetail.common.components.LabeledSection
import com.example.rentit.presentation.rentaldetail.common.components.TaskCheckBox

@Composable
fun TaskSection(
    title: String,
    guideText: String,
    policyText: String? = null,
    photoTaskLabel: String,
    trackingNumTaskLabel: String,
    isReturnAvailable: Boolean,
    isPhotoRegistered: Boolean,
    isTrackingNumRegistered: Boolean,
    content: @Composable () -> Unit
) {
    val returnRegCountText = listOf(isPhotoRegistered, isTrackingNumRegistered)
        .count { it }
        .let { "($it/2)" }

    LabeledSection(
        labelText = buildAnnotatedString {
            append("$title $returnRegCountText")
        }
    ) {
        content()
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = guideText,
            style = MaterialTheme.typography.labelMedium
        )
        TaskCheckBox(
            taskText = photoTaskLabel,
            isTaskEnable = isReturnAvailable,
            isDone = isPhotoRegistered,
        )
        TaskCheckBox(
            taskText = trackingNumTaskLabel,
            isTaskEnable = isReturnAvailable,
            isDone = isTrackingNumRegistered
        )
        if (!policyText.isNullOrEmpty())
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = policyText,
                style = MaterialTheme.typography.labelSmall,
                color = Gray400
            )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    RentItTheme {
        TaskSection(
            title = "반납 정보",
            guideText = "반납 사진과 운송장 번호를 모두 등록해 주세요.",
            policyText = "※ 등록 후에는 수정이 어렵습니다.",
            photoTaskLabel = "반납 사진 등록",
            trackingNumTaskLabel = "운송장 번호 입력",
            isReturnAvailable = true,
            isPhotoRegistered = true,
            isTrackingNumRegistered = false
        ) {
            // 예시 content — 실제 사용 시엔 이미지나 기타 content
            Text(
                text = "이곳은 반납 관련 콘텐츠가 들어갈 영역입니다.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}