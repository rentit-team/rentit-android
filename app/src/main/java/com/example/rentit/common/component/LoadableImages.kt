package com.example.rentit.common.component

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rentit.R

@Composable
fun LoadableUrlImage(
    modifier: Modifier = Modifier,
    imgUrl: String?,
    @DrawableRes defaultImageResId: Int,
    @StringRes defaultDescResId: Int = R.string.common_list_item_thumbnail_img_placeholder_description,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(imgUrl)
            .error(defaultImageResId)
            .placeholder(defaultImageResId)
            .fallback(defaultImageResId)
            .build(),
        contentDescription = stringResource(id = defaultDescResId),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun LoadableUriImage(
    modifier: Modifier = Modifier,
    imgUri: Uri?,
    @StringRes defaultDescResId: Int = R.string.common_loadable_uri_img_content_description,
) {
    val defaultImageResId = R.drawable.img_photo_load_fail
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(imgUri)
            .error(defaultImageResId)
            .placeholder(defaultImageResId)
            .fallback(defaultImageResId)
            .build(),
        contentDescription = stringResource(id = defaultDescResId),
        contentScale = ContentScale.Crop
    )
}

