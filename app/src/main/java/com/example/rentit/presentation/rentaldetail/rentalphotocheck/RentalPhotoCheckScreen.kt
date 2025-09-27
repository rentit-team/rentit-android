package com.example.rentit.presentation.rentaldetail.rentalphotocheck

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.ui.component.item.RentItBasicButton
import com.example.rentit.common.ui.component.layout.RentItTopAppBar
import com.example.rentit.common.ui.component.item.RentItLoadableUrlImage
import com.example.rentit.common.ui.extension.rentItBasicRoundedGrayBorder
import com.example.rentit.common.ui.extension.rentItScreenHorizontalPadding
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.White

@Composable
fun RentalPhotoCheckScreen(
    totalPageCnt: Int,
    currentPageNumber: Int,
    isNextAvailable: Boolean,
    isBackAvailable: Boolean,
    beforePhotoUrl: String?,
    afterPhotoUrl: String?,
    previewPhotoUrl: String?,
    onPhotoClick: (String?) -> Unit,
    onPageNext: () -> Unit,
    onPageBack: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = { RentItTopAppBar { onBackPressed() } },
        bottomBar = {
            BottomButtons(
                currentPageNumber = currentPageNumber,
                totalPageCnt = totalPageCnt,
                isNextAvailable = isNextAvailable,
                isBackAvailable = isBackAvailable,
                onPageNext = onPageNext,
                onPageBack = onPageBack
            )
        }
    ) {
        Column(Modifier.padding(it).rentItScreenHorizontalPadding()){

            PhotoGuideText(totalPageCnt, currentPageNumber)

            PhotoPreviewBox(previewPhotoUrl)

            BeforeAfterPhoto(beforePhotoUrl, afterPhotoUrl, onPhotoClick)
        }
    }
}

@Composable
private fun PhotoGuideText(totalPageCnt: Int, currentPageNumber: Int) {
    Text(
        modifier = Modifier.padding(top = 16.dp, bottom = 14.dp),
        text = stringResource(
            R.string.screen_rental_photo_check_title,
            currentPageNumber,
            totalPageCnt
        ),
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        modifier = Modifier.padding(bottom = 20.dp),
        text = stringResource(R.string.screen_rental_photo_check_info),
        style = MaterialTheme.typography.labelMedium
    )
}

@Composable
private fun PhotoPreviewBox(photoUrl: String?) {
    val boxContentDesc =
        stringResource(R.string.screen_rental_photo_check_photo_preview_content_description)
    Box(
        Modifier
            .semantics { contentDescription = boxContentDesc }
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .clip(RoundedCornerShape(20.dp))
            .rentItBasicRoundedGrayBorder(),
    ) {
        RentItLoadableUrlImage(
            modifier = Modifier.fillMaxSize(),
            imgUrl = photoUrl,
            defaultImageResId = R.drawable.img_placeholder,
        )
    }
}

@Composable
private fun BeforeAfterPhoto(
    beforePhotoUrl: String?,
    afterPhotoUrl: String?,
    onPhotoClick: (String?) -> Unit,
) {
    @Composable
    fun LabeledPhotoBox(photoUrl: String?, labelText: String, onClick: (String?) -> Unit) {
        val boxContentDesc = stringResource(R.string.screen_rental_photo_check_photo_box_content_description, labelText)
        Box(
            Modifier
                .semantics { contentDescription = boxContentDesc }
                .width(140.dp)
                .aspectRatio(4f / 3f)
                .clip(RoundedCornerShape(20.dp))
                .rentItBasicRoundedGrayBorder()
                .clickable { onClick(photoUrl) }
        ) {
            RentItLoadableUrlImage(
                modifier = Modifier.fillMaxWidth(),
                imgUrl = photoUrl,
                defaultImageResId = R.drawable.img_placeholder,
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Gray400.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(labelText, style = MaterialTheme.typography.bodyLarge, color = White)
            }
        }
    }

    Row(
        Modifier.padding(top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        LabeledPhotoBox(
            beforePhotoUrl,
            stringResource(R.string.screen_rental_photo_check_rental_before_label),
            onPhotoClick
        )
        LabeledPhotoBox(
            afterPhotoUrl,
            stringResource(R.string.screen_rental_photo_check_rental_after_label),
            onPhotoClick
        )
    }
}

@Composable
private fun BottomButtons(
    currentPageNumber: Int,
    totalPageCnt: Int,
    isNextAvailable: Boolean,
    isBackAvailable: Boolean,
    onPageBack: () -> Unit,
    onPageNext: () -> Unit,
) {
    Row(
        Modifier
            .rentItScreenHorizontalPadding()
            .padding(bottom = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (isBackAvailable) {
            RentItBasicButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.screen_rental_photo_check_btn_back),
                containerColor = PrimaryBlue500,
                contentColor = White
            ) { onPageBack() }
        }
        if (isNextAvailable) {
            RentItBasicButton(
                modifier = Modifier.weight(1f),
                text = stringResource(
                    R.string.screen_rental_photo_check_btn_next,
                    currentPageNumber,
                    totalPageCnt
                ),
                containerColor = PrimaryBlue500,
                contentColor = White,
            ) { onPageNext() }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RentalPhotoCheckScreenPreview() {
    RentItTheme {
        RentalPhotoCheckScreen(
            totalPageCnt = 3,
            currentPageNumber = 1,
            isNextAvailable = true,
            isBackAvailable = true,
            beforePhotoUrl = "",
            afterPhotoUrl = "",
            previewPhotoUrl = "",
            onPhotoClick = { },
            onPageNext = { },
            onPageBack = { },
            onBackPressed = { },
        )
    }
}
