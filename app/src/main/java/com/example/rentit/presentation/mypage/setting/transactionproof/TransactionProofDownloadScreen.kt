package com.example.rentit.presentation.mypage.setting.transactionproof

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonDivider
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.RentalSummary
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.uimodel.RentalSummaryUiModel
import com.example.rentit.common.theme.RentItTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionProofDownloadScreen(
    transactionRecords: List<RentalSummaryUiModel>,
    onBackPressed: () -> Unit = {},
    onDownloadClick: () -> Unit = {},
) {
    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(R.string.screen_transaction_proof_title), onBackClick = onBackPressed) }
    ) {
        Column(Modifier
            .padding(it)
            .verticalScroll(rememberScrollState())) {
            transactionRecords.forEach { record ->
                CommonDivider()
                TransactionProofDownloadListItem(record, onDownloadClick)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionProofDownloadListItem(rentalSummary: RentalSummaryUiModel, onDownloadClick: () -> Unit) {

    val downloadBtnDescription = stringResource(
        R.string.screen_transaction_proof_download_btn_description,
        rentalSummary.startDate,
        rentalSummary.endDate,
        rentalSummary.productTitle
    )

    Row(Modifier
        .screenHorizontalPadding()
        .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        RentalSummary(
            modifier = Modifier.weight(1f),
            thumbnailImgUrl = rentalSummary.thumbnailImgUrl,
            productTitle = rentalSummary.productTitle,
            startDate = rentalSummary.startDate,
            endDate = rentalSummary.endDate,
            totalPrice = rentalSummary.totalPrice
        )
        IconButton(
            onClick = onDownloadClick,
            modifier = Modifier.semantics { contentDescription = downloadBtnDescription },
        ) {
            Icon(painterResource(R.drawable.ic_download), contentDescription = stringResource(R.string.content_description_for_ic_download))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun TransactionProofDownloadScreenPreview() {
    val sampleRentalSummaries = listOf(
        RentalSummaryUiModel(
            productTitle = "프리미엄 캠핑 텐트",
            thumbnailImgUrl = "https://example.com/images/tent.jpg",
            startDate = "2025-08-01",
            endDate = "2025-08-03",
            totalPrice = 50000
        ),
        RentalSummaryUiModel(
            productTitle = "캐논 DSLR 카메라",
            thumbnailImgUrl = "https://example.com/images/camera.jpg",
            startDate = "2025-08-05",
            endDate = "2025-08-07",
            totalPrice = 75000
        ),
        RentalSummaryUiModel(
            productTitle = "전동 킥보드",
            thumbnailImgUrl = "https://example.com/images/kickboard.jpg",
            startDate = "2025-08-10",
            endDate = "2025-08-12",
            totalPrice = 30000
        )
    )

    RentItTheme {
        TransactionProofDownloadScreen(sampleRentalSummaries)
    }
}