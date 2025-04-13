package com.example.rentit.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.rentit.R

val pretendardFamily = FontFamily(
    Font(R.font.pretendard_thin, FontWeight.Thin),
    Font(R.font.pretendard_extra_light, FontWeight.ExtraLight),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold),
    Font(R.font.pretendard_extra_bold, FontWeight.ExtraBold),
)

object pretendardTextStyle {

    // Title - Bold, 28pt
    val title: TextStyle = TextStyle(
        fontFamily = pretendardFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        //lineHeight = 36.sp
    )

    // Headline_1 - Semi Bold, 24pt
    val headline_bold: TextStyle = TextStyle(
        fontFamily = pretendardFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
    )

    // Headline_2 - Regular, 24pt
    val headline: TextStyle = TextStyle(
        fontFamily = pretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
    )

    // Subhead_1 - Semi Bold, 20pt
    val subhead_bold: TextStyle = TextStyle(
        fontFamily = pretendardFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    )

    // Subhead_2 - Regular, 20pt
    val subhead: TextStyle = TextStyle(
        fontFamily = pretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
    )

    // Body1_1 - Semi Bold, 16pt
    val body1_bold: TextStyle = TextStyle(
        fontFamily = pretendardFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    )

    // Body1_2 - Regular, 16pt
    val body1: TextStyle = TextStyle(
        fontFamily = pretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )

    // Body2_1 - Semi Bold, 12pt
    val body2_bold: TextStyle = TextStyle(
        fontFamily = pretendardFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
    )

    // Body2_2 - Regular, 12pt
    val body2: TextStyle = TextStyle(
        fontFamily = pretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    )

    // Caption - Bold, 10pt
    val caption_bold: TextStyle = TextStyle(
        fontFamily = pretendardFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
    )

    // Caption - Regular, 10pt
    val caption: TextStyle = TextStyle(
        fontFamily = pretendardFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
    )
}

// Set of Material typography styles to start with
val AppTypography = Typography(
    displayLarge = pretendardTextStyle.title,           // 28pt Bold
    headlineLarge = pretendardTextStyle.headline,       // 24pt SemiBold
    headlineMedium = pretendardTextStyle.headline_bold, // 24pt Regular
    titleLarge = pretendardTextStyle.subhead_bold,      // 20pt SemiBold
    titleMedium = pretendardTextStyle.subhead,          // 20pt Regular
    bodyLarge = pretendardTextStyle.body1_bold,         // 16pt SemiBold
    bodyMedium = pretendardTextStyle.body1,             // 16pt Regular
    labelLarge = pretendardTextStyle.body2_bold,        // 12pt SemiBold
    labelMedium = pretendardTextStyle.body2,            // 12pt Regular
    labelSmall = pretendardTextStyle.caption,           // 10pt Regular
)
