package com.example.rentit.presentation.mypage.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

private fun Modifier.listItemModifier(
    contentDesc: String,
    isClickEnabled: Boolean = true,
    onClick: () -> Unit = {},
) = this.semantics { contentDescription = contentDesc }
    .clickable(enabled = isClickEnabled) { onClick() }
    .padding(vertical = 20.dp)
    .screenHorizontalPadding()

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

    Row(Modifier.listItemModifier(contentDesc = contentDesc, onClick = onClick)) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
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
    
    Row(Modifier.listItemModifier(contentDesc = label, isClickEnabled = false)) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
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
    
    Row(Modifier.listItemModifier(contentDesc = label, onClick = onLogoutClick)) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SettingScreenPreview() {
    RentItTheme {
        SettingScreen()
    }
}