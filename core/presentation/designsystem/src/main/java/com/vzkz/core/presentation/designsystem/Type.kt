package com.vzkz.core.presentation.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Exo2 = FontFamily(
    Font(resId = R.font.exo2light, weight = FontWeight.Light),
    Font(resId = R.font.exo2regular, weight = FontWeight.Normal),
    Font(resId = R.font.exo2medium, weight = FontWeight.Medium),
    Font(resId = R.font.exo2semibold, weight = FontWeight.SemiBold),
    Font(resId = R.font.exo2bold, weight = FontWeight.Bold),
)

val Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = Exo2,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        color = BeePadelGray
    ),
    bodyMedium = TextStyle(
        fontFamily = Exo2,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Exo2,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Exo2,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Exo2,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        color = BeePadelWhite
    ),
)
