package com.example.rentit.presentation.main.createpost.drawer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.domain.product.model.CategoryModel
import com.example.rentit.presentation.main.createpost.LabeledContent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryTagDrawer(
    categoryMap: Map<Int, CategoryModel>,
    selectedCategoryList: List<Int> = emptyList(),
    onTagButtonClick: (Int) -> Unit
) {
    val parentCatList = categoryMap.filter { it.value.isParent }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .screenHorizontalPadding()
            .padding(bottom = 80.dp)
    ) {
        parentCatList.forEach { parentCat ->
            val childCatList = categoryMap.filter { cat -> parentCat.key == cat.value.parentId }
            LabeledContent(title = parentCat.value.name) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    childCatList.forEach { cat ->
                        TagButton(
                            text = cat.value.name,
                            isSelectedTag = selectedCategoryList.contains(cat.key),
                            onClick = { onTagButtonClick(cat.key) })
                    }
                }
            }
        }
        if(categoryMap.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 6.dp),
                    painter = painterResource(R.drawable.ic_info),
                    contentDescription = null,
                    tint = Gray400
                )
                Text(
                    text = stringResource(id = R.string.drawer_category_tag_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray400,
                )
            }
        }
    }
}

@Composable
fun TagButton(
    text: String,
    isSelectedTag: Boolean = false,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(1.dp, if(isSelectedTag) PrimaryBlue500 else Gray200),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelectedTag) {
                Icon(
                    modifier = Modifier
                        .height(12.dp)
                        .padding(end = 6.dp),
                    painter = painterResource(id = R.drawable.ic_check),
                    tint = PrimaryBlue500,
                    contentDescription = stringResource(id = R.string.common_tag_btn_icon_check_description)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelectedTag) PrimaryBlue500 else AppBlack
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCategoryTagDrawer() {
    RentItTheme {
        CategoryTagDrawer(categoryMap = emptyMap()){}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTagButton() {
    RentItTheme {
        TagButton("버튼", false) {}
    }
}