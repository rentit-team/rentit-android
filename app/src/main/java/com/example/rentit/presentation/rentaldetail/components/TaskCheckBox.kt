package com.example.rentit.presentation.rentaldetail.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

/**
 * 라벨과 구분선이 포함된 섹션 레이아웃
 */

private val cardVerticalPadding = 2.dp
private val cardCornerRadius = 12.dp
private val innerVerticalPadding = 8.dp
private val spacerSize = 8.dp

@Composable
fun TaskCheckBox(
    modifier: Modifier = Modifier,
    isTaskEnable: Boolean,
    taskText: String,
    isDone: Boolean = false,
    onClick: () -> Unit = {},
) {
    val iconTint = if (isDone) PrimaryBlue500 else Gray300
    val iconDescription =
        if (isDone) stringResource(R.string.screen_rental_detail_task_check_box_icon_done, taskText)
        else stringResource(R.string.screen_rental_detail_task_check_box_icon_not_done, taskText)
    val isButtonEnable = !isDone && isTaskEnable

    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = cardVerticalPadding)
            .clip(RoundedCornerShape(cardCornerRadius)),
        shape = RoundedCornerShape(cardCornerRadius),
        border = BorderStroke(2.dp, Gray100),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            disabledContainerColor = Gray100
        ),
        onClick = onClick,
        enabled = isButtonEnable
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = innerVerticalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(taskText, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.size(spacerSize))
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.ic_check_circle_bold),
                contentDescription = iconDescription,
                tint = iconTint
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    RentItTheme {
        TaskCheckBox(
            taskText = "대여 전 사진 등록",
            isTaskEnable = true,
        )
    }
}