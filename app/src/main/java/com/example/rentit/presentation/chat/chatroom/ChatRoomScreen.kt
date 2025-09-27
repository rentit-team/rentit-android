package com.example.rentit.presentation.chat.chatroom

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.ui.component.layout.RentItTopAppBar
import com.example.rentit.common.ui.component.item.RentItLoadableUrlImage
import com.example.rentit.common.ui.formatter.formatPeriodText
import com.example.rentit.common.ui.extension.rentItScreenHorizontalPadding
import com.example.rentit.common.enums.ProductStatus
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.daysFromToday
import com.example.rentit.common.ui.formatter.priceFormatter
import com.example.rentit.common.ui.formatter.rentalPeriodFormatter
import com.example.rentit.domain.chat.model.ChatMessageModel
import com.example.rentit.domain.chat.model.ChatRoomProductSummaryModel
import com.example.rentit.domain.chat.model.ChatRoomRentalSummaryModel
import com.example.rentit.common.enums.RentingStatus
import com.example.rentit.presentation.chat.chatroom.components.ReceivedMessageBubble
import com.example.rentit.presentation.chat.chatroom.components.SentMessageBubble
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatroomScreen(
    messageText: TextFieldValue,
    partnerNickname: String?,
    messages: List<ChatMessageModel>,
    productSummary: ChatRoomProductSummaryModel?,
    rentalSummary: ChatRoomRentalSummaryModel?,
    messageScrollState: LazyListState,
    inputScrollState: ScrollState,
    onPayClick: () -> Unit,
    onProductSectionClick: () -> Unit,
    onRentalSectionClick: () -> Unit,
    onMessageChange: (TextFieldValue) -> Unit,
    onMessageSend: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = { RentItTopAppBar(onBackClick = navigateBack) },
        bottomBar = {
            BottomInputBar(
                value = messageText,
                scrollState = inputScrollState,
                onValueChange = onMessageChange,
                onSend = onMessageSend
            )
        }
    ) {
        if(productSummary != null && rentalSummary != null) {
            Column(Modifier.padding(it)) {
                ProductInfoSection(
                    thumbnailImgUrl = productSummary.thumbnailImgUrl,
                    title = productSummary.title,
                    status = productSummary.status,
                    price = productSummary.price,
                    minPeriod = productSummary.minPeriod,
                    maxPeriod = productSummary.maxPeriod,
                    onSectionClick = onProductSectionClick
                )

                RequestInfo(
                    status = rentalSummary.status,
                    startDate = rentalSummary.startDate,
                    endDate = rentalSummary.endDate,
                    onSectionClick = onRentalSectionClick,
                )

                ChatMessageList(
                    modifier = Modifier.weight(1f),
                    messageScrollState = messageScrollState,
                    partnerNickname = partnerNickname,
                    messageList = messages,
                    onPayClick = onPayClick
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ProductInfoSection(
    thumbnailImgUrl: String?,
    title: String,
    status: ProductStatus,
    price: Int,
    minPeriod: Int?,
    maxPeriod: Int?,
    onSectionClick: () -> Unit
) {
    val priceText = priceFormatter(price)
    val unitText = stringResource(R.string.common_price_unit_per_day)
    val periodText = formatPeriodText(minPeriod, maxPeriod)

    Row(
        modifier = Modifier
            .rentItScreenHorizontalPadding()
            .padding(bottom = 20.dp)
            .clickable { onSectionClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RentItLoadableUrlImage(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(15.dp)),
            imgUrl = thumbnailImgUrl,
            defaultImageResId = R.drawable.img_thumbnail_placeholder,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = status.label ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = PrimaryBlue500
                )
                Text(
                    modifier = Modifier.width(160.dp),
                    maxLines = 1,
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                modifier = Modifier
                .padding(top = 14.dp),
                text = "$priceText$unitText ($periodText)",
                style = MaterialTheme.typography.bodyLarge,
                color = Gray400
            )
        }
    }
}

// 요청 정보
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun RequestInfo(
    status: RentalStatus,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onSectionClick: () -> Unit
) {
    val periodText = rentalPeriodFormatter(LocalContext.current, startDate, endDate)

    val rentingStatus = status.takeIf { status == RentalStatus.RENTING }?.let {
        val daysFromReturnDate = daysFromToday(endDate)
        RentingStatus.fromDaysFromReturnDate(daysFromReturnDate)
    }

    val statusTextRes = rentingStatus?.let { rentingStatus.strRes } ?: status.strRes
    val statusColor = rentingStatus?.let { rentingStatus.textColor } ?: status.color

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .rentItScreenHorizontalPadding()
            .padding(bottom = 20.dp)
            .clickable { onSectionClick() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(statusTextRes),
            style = MaterialTheme.typography.labelLarge,
            color = statusColor
        )
        Text(
            text = periodText,
            style = MaterialTheme.typography.labelMedium,
            color = Gray800
        )
    }
}

// 채팅 메세지 리스트
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ChatMessageList(
    modifier: Modifier,
    messageScrollState: LazyListState,
    partnerNickname: String?,
    messageList: List<ChatMessageModel>,
    onPayClick: () -> Unit
) {
    val displayPartnerNickname = partnerNickname ?: stringResource(R.string.screen_chatroom_nickname_unknown)
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(Gray100)
            .padding(horizontal = 15.dp),
        reverseLayout = true,
        state = messageScrollState
    ) {
        items(messageList, key = { it.messageId }) { msg ->
            if (msg.isMine) {
                SentMessageBubble(msg.message, msg.sentAt)
            } else {
                ReceivedMessageBubble(msg.message, msg.sentAt, displayPartnerNickname, onPayClick)
            }
        }
    }
}

@Composable
private fun BottomInputBar(
    value: TextFieldValue,
    scrollState: ScrollState,
    onValueChange: (TextFieldValue) -> Unit = {},
    onSend: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .rentItScreenHorizontalPadding()
            .padding(top = 14.dp, bottom = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .heightIn(min = 34.dp, max = 110.dp)
                .verticalScroll(scrollState)
                .weight(1F),
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            ),
            maxLines = Int.MAX_VALUE,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Gray100)
                        .padding(20.dp, 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.screen_chatroom_input_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Gray400
                        )
                    }
                    innerTextField()
                }
            }
        )
        IconButton(
            modifier = Modifier.weight(0.1F), onClick = onSend) {
            Icon(
                painter = painterResource(R.drawable.ic_send),
                tint = Gray800,
                contentDescription = stringResource(R.string.content_description_for_ic_send)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun ChatRoomScreenPreview() {
    // 샘플 메시지
    val sampleMessages = listOf(
        ChatMessageModel(
            messageId = "01",
            isMine = true,
            message = "안녕하세요! 예약 확인 부탁드려요.",
            sentAt = "2025-03-25T09:00:00Z"
        ),
        ChatMessageModel(
            messageId = "02",
            isMine = false,
            message = "네, 예약 확인되었습니다.",
            sentAt = "2025-03-25T09:00:00Z"
        ),
        ChatMessageModel(
            messageId = "02",
            isMine = true,
            message = "감사합니다!",
            sentAt = "2025-03-25T09:00:00Z"
        )
    )

// 샘플 상품 요약
    val sampleProduct = ChatRoomProductSummaryModel(
        thumbnailImgUrl = "https://example.com/product_thumbnail.jpg",
        title = "캠핑용 텐트",
        status = ProductStatus.AVAILABLE,
        price = 50000,
        minPeriod = 1,
        maxPeriod = 7,
        productId = 0
    )

// 샘플 대여 요약
    val sampleRental = ChatRoomRentalSummaryModel(
        reservationId = 12345,
        status = RentalStatus.PENDING,
        startDate = LocalDate.of(2025, 9, 1),
        endDate = LocalDate.of(2025, 9, 5),
        renterId = 0
    )

// 샘플 파라미터
    val samplePartnerNickname = "홍길동"
    val sampleMessageInput = TextFieldValue("")
    val sampleScrollState = rememberScrollState()

// ChatroomScreen 호출 예시
    RentItTheme {
        ChatroomScreen(
            messageText = sampleMessageInput,
            partnerNickname = samplePartnerNickname,
            messages = sampleMessages,
            productSummary = sampleProduct,
            rentalSummary = sampleRental,
            messageScrollState = rememberLazyListState(),
            inputScrollState = sampleScrollState,
            navigateBack = { },
            onPayClick = { },
            onProductSectionClick = { },
            onRentalSectionClick = { },
            onMessageChange = { },
            onMessageSend = { },
        )
    }
}