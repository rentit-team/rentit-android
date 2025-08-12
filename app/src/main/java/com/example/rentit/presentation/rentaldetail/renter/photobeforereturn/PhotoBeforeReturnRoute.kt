package com.example.rentit.presentation.rentaldetail.renter.photobeforereturn

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.rentit.presentation.rentaldetail.components.rememberTakePhotoLauncher

@Composable
fun PhotoBeforeReturnRoute() {

    // 임시 이미지 리스트 (API 연동 후 제거)
    val imgBeforeRentList = listOf(
        "https://media.istockphoto.com/id/520700958/ko/%EC%82%AC%EC%A7%84/%EC%95%84%EB%A6%84%EB%8B%A4%EC%9A%B4-%EA%BD%83-%EB%B0%B0%EA%B2%BD%EA%B8%B0%EC%88%A0.jpg?s=1024x1024&w=is&k=20&c=Ci4unh6xc-hSxWAdqbY2CDGG4_8j8HnG1mPh4gbWYHs=",
        "https://media.istockphoto.com/id/916766854/ko/%EC%82%AC%EC%A7%84/%ED%95%98%EB%8A%98%EA%B3%BC-%EB%AC%B4%EC%A7%80%EA%B0%9C-%EB%B0%B0%EA%B2%BD.jpg?s=1024x1024&w=is&k=20&c=NCkA_Kd89y--eZb51AXzjTSreKPFCmACwm3scBJ9MXs=",
        "https://media.istockphoto.com/id/1208790371/ko/%EC%82%AC%EC%A7%84/%EA%B1%B4%EA%B0%95-%ED%95%9C-%EC%8B%A0%EC%84%A0%ED%95%9C-%EB%AC%B4%EC%A7%80%EA%B0%9C-%EC%83%89%EA%B9%94%EC%9D%98-%EA%B3%BC%EC%9D%BC%EA%B3%BC-%EC%95%BC%EC%B1%84-%EB%B0%B0%EA%B2%BD.jpg?s=1024x1024&w=is&k=20&c=YXOb4qT32YzXhh3iwJGo7IMtBWALt33f9DmhuyWHR70=",
    )

    var currentPageIndex by remember { mutableIntStateOf(0) }
    var takenPhotoUris by remember { mutableStateOf(List(imgBeforeRentList.size) { Uri.EMPTY }) }
    val takenPhotoCnt by remember(takenPhotoUris) { mutableIntStateOf(takenPhotoUris.count { it != Uri.EMPTY }) }

    val isLastPage = currentPageIndex == imgBeforeRentList.size - 1
    val isNextAvailable = takenPhotoUris[currentPageIndex] != Uri.EMPTY
    val isBackAvailable = currentPageIndex > 0

    fun updatePhotoAtIndex(index: Int, uri: Uri) {
        takenPhotoUris = takenPhotoUris.toMutableList().apply { set(index, uri) }
    }

    val launchCamera = rememberTakePhotoLauncher { uri -> updatePhotoAtIndex(currentPageIndex, uri) }

    // currentPageNumber: 1-based 페이지 번호 (사용자에게 표시되는 값)
    PhotoBeforeReturnScreen(
        currentPageNumber = currentPageIndex + 1,
        requiredPhotoCnt = imgBeforeRentList.size,
        takenPhotoCnt = takenPhotoCnt,
        beforePhotoUrl = imgBeforeRentList[currentPageIndex],
        takenPhotoUri = takenPhotoUris[currentPageIndex],
        isLastPage = isLastPage,
        isNextAvailable = isNextAvailable,
        isBackAvailable = isBackAvailable,
        onPageNext = { if(currentPageIndex < imgBeforeRentList.lastIndex) currentPageIndex += 1 },
        onPageBack = { if(currentPageIndex > 0) currentPageIndex -= 1 },
        onTakePhoto = { launchCamera() },
        onComplete = { },   // API 연동 후 추가 예정
    )
}