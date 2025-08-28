package com.example.rentit.presentation.main.createpost.categorytag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.product.dto.CategoryDto
import com.example.rentit.presentation.main.createpost.LabeledContent
import com.example.rentit.presentation.main.createpost.components.TagButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryTagDrawer(
    categoryList: List<CategoryDto>,
    selectedCategoryList: List<CategoryDto> = emptyList(),
    onTagButtonClick: (CategoryDto) -> Unit
) {
    val parentCatList = categoryList.filter { it.isParent }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .screenHorizontalPadding()
            .padding(bottom = 80.dp)
    ) {
        parentCatList.forEach {
            val childCatList = categoryList.filter { cat -> it.id == cat.parentId }
            LabeledContent(title = it.name) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    childCatList.forEach { cat ->
                        TagButton(
                            text = cat.name,
                            isSelected = selectedCategoryList.contains(cat),
                            onClick = { onTagButtonClick(cat) })
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCategoryTagDrawer() {
    RentItTheme {
        CategoryTagDrawer(categoryList = emptyList()){}
    }
}