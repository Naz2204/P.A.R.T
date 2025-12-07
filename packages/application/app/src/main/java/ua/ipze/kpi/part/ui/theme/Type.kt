package ua.ipze.kpi.part.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ua.ipze.kpi.part.R
// Set of Material typography styles to start with

val AppFont = FontFamily(
    Font(R.font.pixel_font_7)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = AppFont
    ),
    bodyMedium = TextStyle(
        fontFamily = AppFont
    ),
    bodySmall = TextStyle(
        fontFamily = AppFont
    ),
    titleLarge = TextStyle(
        fontFamily = AppFont
    ),
    titleMedium = TextStyle(
        fontFamily = AppFont
    ),
    titleSmall = TextStyle(
        fontFamily = AppFont
    )
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)