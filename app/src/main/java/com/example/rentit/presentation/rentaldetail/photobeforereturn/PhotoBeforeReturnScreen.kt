package com.example.rentit.presentation.rentaldetail.photobeforereturn

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.LoadableUriImage
import com.example.rentit.common.component.LoadableUrlImage
import com.example.rentit.common.component.basicRoundedGrayBorder
import com.example.rentit.common.component.dialog.FullImageDialog
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.White

@Composable
fun PhotoBeforeReturnScreen(
    requiredPhotoCnt: Int,
    currentPageNumber: Int,
    takenPhotoCnt: Int,
    beforePhotoUrl: String,
    takenPhotoUri: Uri,
    showFullImageDialog: Boolean,
    isLastPage: Boolean,
    isRegisterAvailable: Boolean,
    isNextAvailable: Boolean,
    isBackAvailable: Boolean,
    onFullImageDialogDismiss: () -> Unit,
    onPageNext: () -> Unit,
    onPageBack: () -> Unit,
    onBeforeImageClick: () -> Unit,
    onTakePhoto: () -> Unit,
    onRegister: () -> Unit
) {
    Scaffold(
        topBar = { CommonTopAppBar { } },
        bottomBar = { BottomButtons(
            currentPageNumber = currentPageNumber,
            totalPageCnt = requiredPhotoCnt,
            isLastPage = isLastPage,
            isRegisterAvailable = isRegisterAvailable,
            isNextAvailable = isNextAvailable,
            isBackAvailable = isBackAvailable,
            onPageNext = onPageNext,
            onPageBack = onPageBack,
            onRegister = onRegister,
        ) }
    ) {
        Column(
            Modifier
                .padding(it)
                .screenHorizontalPadding()
                .fillMaxHeight(),
        ) {
            Spacer(Modifier.weight(1f))

            PhotoGuideText(takenPhotoCnt, requiredPhotoCnt)

            PhotoWithTakePhotoButton(
                beforePhotoUrl = beforePhotoUrl,
                takenPhotoUri = takenPhotoUri,
                onBeforeImageClick = onBeforeImageClick,
                onTakePhoto = onTakePhoto
            )

            Spacer(Modifier.weight(2f))
        }
    }

    if(showFullImageDialog){
        FullImageDialog(
            imageUrls = beforePhotoUrl,
            onDismiss = onFullImageDialogDismiss
        )
    }
}

@Composable
private fun PhotoGuideText(takenPhotoCnt: Int, requiredPhotoCnt: Int) {
    Text(
        modifier = Modifier.padding(top = 16.dp, bottom = 14.dp),
        text = stringResource(R.string.screen_photo_before_return_title, takenPhotoCnt, requiredPhotoCnt),
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        modifier = Modifier.padding(bottom = 50.dp),
        text = stringResource(R.string.screen_photo_before_return_guide),
        style = MaterialTheme.typography.labelMedium.copy(lineHeight = 20.sp)
    )
}


@Composable
private fun BottomButtons(
    currentPageNumber: Int,
    totalPageCnt: Int,
    isLastPage: Boolean,
    isRegisterAvailable: Boolean,
    isNextAvailable: Boolean,
    isBackAvailable: Boolean,
    onPageBack: () -> Unit,
    onPageNext: () -> Unit,
    onRegister: () -> Unit,
) {
    val nextBtnText = stringResource(R.string.screen_photo_before_return_btn_next_with_index, currentPageNumber, totalPageCnt)

    Row(
        Modifier
            .screenHorizontalPadding()
            .padding(bottom = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if(isBackAvailable){
            CommonButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.screen_photo_before_return_btn_back),
                containerColor = PrimaryBlue500,
                contentColor = White
            ) { onPageBack() }
        }
        CommonButton(
            modifier = Modifier.weight(1f),
            text = if (isLastPage) stringResource(R.string.screen_photo_before_return_btn_complete) else nextBtnText,
            enabled = if(isLastPage) isRegisterAvailable else isNextAvailable,
            containerColor = if (isNextAvailable) PrimaryBlue500 else Gray200,
            contentColor = White,
            onClick = if (isLastPage) onRegister else onPageNext
        )
    }
}

@Composable
fun PhotoWithTakePhotoButton(
    beforePhotoUrl: String?,
    takenPhotoUri: Uri,
    onBeforeImageClick: () -> Unit = {},
    onTakePhoto: () -> Unit,
) {
    val btnContentDesc = stringResource(R.string.screen_photo_before_rent_take_photo_btn_content_description)

    Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
        Box(Modifier
            .photoBoxModifier()
            .weight(1F)
            .clickable { onBeforeImageClick() }
        ) {
            LoadableUrlImage(
                modifier = Modifier.fillMaxSize(),
                imgUrl = beforePhotoUrl,
                defaultImageResId = R.drawable.img_photo_load_fail,
                defaultDescResId = R.string.screen_product_create_selected_image_description,
            )
        }

        Box(Modifier
            .photoBoxModifier()
            .semantics { contentDescription = btnContentDesc }
            .weight(1F)
            .clickable { onTakePhoto() },
            contentAlignment = Alignment.Center
        ) {
            if (takenPhotoUri == Uri.EMPTY) {
                Icon(
                    painter = painterResource(R.drawable.ic_camera),
                    contentDescription = stringResource(R.string.content_description_for_ic_camera),
                    tint = Gray300
                )
            } else {
                LoadableUriImage(
                    modifier = Modifier.fillMaxSize(),
                    imgUri = takenPhotoUri,
                    defaultDescResId = R.string.screen_photo_before_return_taken_photo_content_description
                )
            }
        }
    }
}

// 화면 내 사진 박스에 적용하는 Modifier 스타일
private fun Modifier.photoBoxModifier() = this
    .fillMaxWidth()
    .aspectRatio(4f / 3f)
    .clip(RoundedCornerShape(20.dp))
    .basicRoundedGrayBorder()

@Composable
@Preview(showBackground = true)
fun PhotoBeforeReturnScreenPreview() {
    val dummyImgList = listOf("url1", "url2", "url3")

    RentItTheme {
        PhotoBeforeReturnScreen(
            currentPageNumber = 1,
            requiredPhotoCnt = dummyImgList.size,
            takenPhotoCnt = 0,
            beforePhotoUrl = dummyImgList[0],
            takenPhotoUri = Uri.EMPTY,
            isLastPage = false,
            isRegisterAvailable = false,
            isNextAvailable = false,
            isBackAvailable = false,
            showFullImageDialog = false,
            onFullImageDialogDismiss = { },
            onPageNext = { },
            onPageBack = { },
            onBeforeImageClick = { },
            onTakePhoto = { },
            onRegister = { },
        )
    }
}