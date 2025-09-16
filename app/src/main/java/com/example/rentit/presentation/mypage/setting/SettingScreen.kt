package com.example.rentit.presentation.mypage.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.RentItTheme

@Composable
fun SettingScreen(
    versionText: String = "v.1.1.0",
    onBackPressed: () -> Unit = {},
    onRegisterAccountClick: () -> Unit = {},
    onCheatingReportFormClick: () -> Unit = {},
    onKeywordAlertSettingClick: () -> Unit = {},
    onSafetyGuideClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(R.string.screen_setting_title), onBackClick = onBackPressed) }
    ) {
        Column(Modifier.padding(it)) {

            SettingListItem(stringResource(R.string.screen_setting_label_register_account), onRegisterAccountClick)

            SettingListItem(stringResource(R.string.screen_setting_label_cheating_report_form), onCheatingReportFormClick)

            SettingListItem(stringResource(R.string.screen_setting_label_keyword_alert_setting), onKeywordAlertSettingClick)

            SettingListItem(stringResource(R.string.screen_setting_label_safety_guide), onSafetyGuideClick)

            VersionInfoListItem(versionText)

            LogoutListItem(onLogoutClick)
        }
    }
}

/**
 * 설정 화면 List Item용 공통 Row
 * (클릭, contentDescription 처리 포함)
 */

@Composable
fun SettingRow(
    label: String,
    contentDesc: String = label,
    isClickEnabled: Boolean = true,
    onClick: () -> Unit = {},
    endContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .semantics { contentDescription = contentDesc }
            .height(65.dp)
            .clickable(enabled = isClickEnabled) { onClick() }
            .screenHorizontalPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        endContent?.invoke()
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