package com.example.rentit.presentation.mypage.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.R
import com.example.rentit.common.component.CommonDivider
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.mypage.setting.components.SettingRow

@Composable
fun SettingScreen(
    versionText: String = "v.1.1.0",
    onBackPressed: () -> Unit = {},
    onRegisterAccountClick: () -> Unit = {},
    onTransactionProofClick: () -> Unit = {},
    onKeywordAlertSettingClick: () -> Unit = {},
    onSafetyGuideClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(R.string.screen_setting_title), onBackClick = onBackPressed) }
    ) {
        Column(Modifier.padding(it)) {

            SettingListItem(stringResource(R.string.screen_setting_label_register_account), onRegisterAccountClick)

            SettingListItem(stringResource(R.string.screen_setting_label_transaction_proof), onTransactionProofClick)

            SettingListItem(stringResource(R.string.screen_setting_label_keyword_alert_setting), onKeywordAlertSettingClick)

            SettingListItem(stringResource(R.string.screen_setting_label_safety_guide), onSafetyGuideClick)

            VersionInfoListItem(versionText)

            LogoutListItem(onLogoutClick)
        }
    }
}

@Composable
private fun SettingListItem(label: String, onClick: () -> Unit = {}) {

    val contentDesc = stringResource(R.string.screen_setting_description_clickable_list_item, label)

    SettingRow(label = label, contentDesc = contentDesc, onClick = onClick){
        Icon(
            painter = painterResource(R.drawable.ic_chevron_right_wide),
            tint = Gray300,
            contentDescription = contentDesc
        )
    }
    CommonDivider(Modifier.screenHorizontalPadding())
}

@Composable
private fun VersionInfoListItem(versionText: String) {
    
    val label = stringResource(R.string.screen_setting_label_version_info)

    SettingRow(label = label, contentDesc = label, isClickEnabled = false){
        Text(
            text = versionText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
    CommonDivider(Modifier.screenHorizontalPadding())
}

@Composable
private fun LogoutListItem(onLogoutClick: () -> Unit = {}) {

    val label = stringResource(R.string.screen_setting_label_logout)

    SettingRow(label = label, contentDesc = label, onClick = onLogoutClick)
}

@Composable
@Preview(showBackground = true)
fun SettingScreenPreview() {
    RentItTheme {
        SettingScreen()
    }
}