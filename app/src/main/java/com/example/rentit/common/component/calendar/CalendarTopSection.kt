package com.example.rentit.common.component.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarHeader(yearMonth: YearMonth, leftChevronOnClick: () -> Unit, rightChevronOnClick: () -> Unit) {
    Row(modifier = Modifier.width(336.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(modifier = Modifier.weight(1F), text = "${yearMonth.year} ${yearMonth.month.value}월", style = MaterialTheme.typography.bodyLarge)
        IconButton(onClick = leftChevronOnClick) {
            Icon(modifier = Modifier.width(7.dp), painter = painterResource(id = R.drawable.ic_chevron_left), contentDescription = stringResource(
                id = R.string.common_calendar_left_chevron_description) )
        }
        IconButton(onClick = rightChevronOnClick) {
            Icon(modifier = Modifier.width(7.dp), painter = painterResource(id = R.drawable.ic_chevron_right), contentDescription = stringResource(
                id = R.string.common_calendar_right_chevron_description) )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayOfWeek(modifier: Modifier) {
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        java.time.DayOfWeek.entries.forEach {
            Text(modifier = Modifier.width(48.dp), text = it.name.take(1), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}