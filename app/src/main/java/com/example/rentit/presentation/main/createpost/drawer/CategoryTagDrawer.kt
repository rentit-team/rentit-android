package com.example.rentit.presentation.main.createpost.drawer

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
import com.example.rentit.domain.product.model.CategoryModel
import com.example.rentit.presentation.main.createpost.LabeledContent
import com.example.rentit.presentation.main.createpost.components.TagButton

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
                            isSelected = selectedCategoryList.contains(cat.key),
                            onClick = { onTagButtonClick(cat.key) })
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
        CategoryTagDrawer(categoryMap = emptyMap()){}
    }
}