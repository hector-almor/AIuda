package com.aiudadores.aiuda.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.aiudadores.aiuda.R

val Josefin = FontFamily(
    Font(R.font.josefinsans_regular, FontWeight.Normal),
    Font(R.font.josefinsans_medium, FontWeight.Medium),
    Font(R.font.josefinsans_bold, FontWeight.Bold),
    Font(R.font.josefinsans_extrabold, FontWeight.ExtraBold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 36.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),

    headlineLarge = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),

    titleLarge = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),

    labelLarge = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Josefin,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )
)