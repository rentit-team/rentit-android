package com.example.rentit.presentation.rentaldetail.dialog

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonTextField
import com.example.rentit.common.component.basicRoundedGrayBorder
import com.example.rentit.common.component.dialog.BaseDialog
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@Composable
fun TrackingRegistrationDialog(
    companyList: List<String>,
    selectedCompany: String?,
    onSelectCompany: (String) -> Unit,
    trackingNum: String,
    onTrackingNumChange: (String) -> Unit,
    onClose: () -> Unit,
    onConfirm: () -> Unit,
) {
    BaseDialog(
        title = stringResource(R.string.dialog_rental_detail_tracking_regs_title),
        confirmBtnText = stringResource(R.string.dialog_rental_detail_tracking_regs_btn_confirm),
        closeBtnText = stringResource(R.string.common_dialog_btn_close),
        onCloseRequest = onClose,
        onConfirmRequest = onConfirm
    ) {
        Text(
            modifier = Modifier.padding(top = 10.dp, bottom = 14.dp),
            text = stringResource(R.string.dialog_rental_detail_tracking_regs_label_delivery_company),
            style = MaterialTheme.typography.bodyLarge
        )
        DeliveryCompanyDropDown(companyList, selectedCompany, onSelectCompany)

        Text(
            modifier = Modifier.padding(top = 20.dp, bottom = 14.dp),
            text = stringResource(R.string.dialog_rental_detail_tracking_regs_label_tracking_num),
            style = MaterialTheme.typography.bodyLarge
        )
        CommonTextField(
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            value = TextFieldValue(trackingNum),
            onValueChange = onTrackingNumChange,
            placeholder = stringResource(R.string.dialog_rental_detail_tracking_regs_placeholder_tracking_num),
            keyboardType = KeyboardType.Number,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryCompanyDropDown(
    companyList: List<String>,
    selectedCompany: String?,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val borderColor = if (expanded) PrimaryBlue500 else Gray200
    val selectedCompanyText = selectedCompany
        ?: stringResource(R.string.dialog_rental_detail_tracking_regs_dropdown_company_default_text)

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .basicRoundedGrayBorder(color = borderColor),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        BasicTextField(
            readOnly = true,
            value = TextFieldValue(selectedCompanyText),
            onValueChange = {},
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.padding(20.dp, 11.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    innerTextField()
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            companyList.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = Color.DKGRAY.toLong())
@Composable
fun TrackingRegistrationDialogPreview() {
    var selectedCompany by remember { mutableStateOf<String?>(null) }
    var trackingNum by remember { mutableStateOf("") }
    val companyList = listOf("대한통운", "한진택배", "우체국택배")
    RentItTheme {
        TrackingRegistrationDialog(
            companyList = companyList,
            selectedCompany = selectedCompany,
            onSelectCompany = { selectedCompany = it },
            trackingNum = trackingNum,
            onTrackingNumChange = { trackingNum = it },
            onClose = { },
            onConfirm = { },
        )
    }
}