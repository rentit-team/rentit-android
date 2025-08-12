package com.example.rentit.presentation.rentaldetail.rentalphotocheck

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.rentit.common.theme.RentItTheme

@Composable
fun RentalPhotoCheckRoute() {
    // 임시 이미지 리스트 (API 연동 후 제거)
    val photoBeforeRentList = listOf(
        "https://media.istockphoto.com/id/520700958/ko/%EC%82%AC%EC%A7%84/%EC%95%84%EB%A6%84%EB%8B%A4%EC%9A%B4-%EA%BD%83-%EB%B0%B0%EA%B2%BD%EA%B8%B0%EC%88%A0.jpg?s=1024x1024&w=is&k=20&c=Ci4unh6xc-hSxWAdqbY2CDGG4_8j8HnG1mPh4gbWYHs=",
        "https://media.istockphoto.com/id/916766854/ko/%EC%82%AC%EC%A7%84/%ED%95%98%EB%8A%98%EA%B3%BC-%EB%AC%B4%EC%A7%80%EA%B0%9C-%EB%B0%B0%EA%B2%BD.jpg?s=1024x1024&w=is&k=20&c=NCkA_Kd89y--eZb51AXzjTSreKPFCmACwm3scBJ9MXs=",
        "https://media.istockphoto.com/id/1208790371/ko/%EC%82%AC%EC%A7%84/%EA%B1%B4%EA%B0%95-%ED%95%9C-%EC%8B%A0%EC%84%A0%ED%95%9C-%EB%AC%B4%EC%A7%80%EA%B0%9C-%EC%83%89%EA%B9%94%EC%9D%98-%EA%B3%BC%EC%9D%BC%EA%B3%BC-%EC%95%BC%EC%B1%84-%EB%B0%B0%EA%B2%BD.jpg?s=1024x1024&w=is&k=20&c=YXOb4qT32YzXhh3iwJGo7IMtBWALt33f9DmhuyWHR70=",
    )

    val photoAfterRentList = listOf(
        "https://media.istockphoto.com/id/916766854/ko/%EC%82%AC%EC%A7%84/%ED%95%98%EB%8A%98%EA%B3%BC-%EB%AC%B4%EC%A7%80%EA%B0%9C-%EB%B0%B0%EA%B2%BD.jpg?s=1024x1024&w=is&k=20&c=NCkA_Kd89y--eZb51AXzjTSreKPFCmACwm3scBJ9MXs=",
        "https://media.istockphoto.com/id/1208790371/ko/%EC%82%AC%EC%A7%84/%EA%B1%B4%EA%B0%95-%ED%95%9C-%EC%8B%A0%EC%84%A0%ED%95%9C-%EB%AC%B4%EC%A7%80%EA%B0%9C-%EC%83%89%EA%B9%94%EC%9D%98-%EA%B3%BC%EC%9D%BC%EA%B3%BC-%EC%95%BC%EC%B1%84-%EB%B0%B0%EA%B2%BD.jpg?s=1024x1024&w=is&k=20&c=YXOb4qT32YzXhh3iwJGo7IMtBWALt33f9DmhuyWHR70=",
        "https://media.istockphoto.com/id/520700958/ko/%EC%82%AC%EC%A7%84/%EC%95%84%EB%A6%84%EB%8B%A4%EC%9A%B4-%EA%BD%83-%EB%B0%B0%EA%B2%BD%EA%B8%B0%EC%88%A0.jpg?s=1024x1024&w=is&k=20&c=Ci4unh6xc-hSxWAdqbY2CDGG4_8j8HnG1mPh4gbWYHs=",
    )

    var currentPageIndex by remember { mutableIntStateOf(0) }
    var previewPhotoUrl by remember(currentPageIndex) { mutableStateOf(photoBeforeRentList.getOrNull(currentPageIndex)) }

    val totalPhotoCnt = minOf(photoBeforeRentList.size, photoAfterRentList.size)
    val isNextAvailable = currentPageIndex < totalPhotoCnt - 1
    val isBackAvailable = currentPageIndex > 0


    RentItTheme {
        RentalPhotoCheckScreen(
            totalPageCnt = totalPhotoCnt,
            currentPageNumber = currentPageIndex + 1,
            isNextAvailable = isNextAvailable,
            isBackAvailable = isBackAvailable,
            beforePhotoUrl = photoBeforeRentList.getOrNull(currentPageIndex),
            afterPhotoUrl = photoAfterRentList.getOrNull(currentPageIndex),
            previewPhotoUrl = previewPhotoUrl,
            onPhotoClick = { previewPhotoUrl = it },
            onPageNext = { if (currentPageIndex < totalPhotoCnt - 1) currentPageIndex += 1 },
            onPageBack = { if (currentPageIndex > 0) currentPageIndex -= 1 },
            onBackPressed = { },    // 화면 간 네비게이션 구현 후 추가 예정
        )
    }
}