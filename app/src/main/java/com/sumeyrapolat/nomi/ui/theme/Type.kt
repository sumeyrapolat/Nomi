package com.sumeyrapolat.nomi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sumeyrapolat.nomi.R

// 🎨 Mulish Variable Font
val Mulish = FontFamily(
    Font(R.font.mulishvariablefontwght , FontWeight.W100),
    Font(R.font.mulishvariablefontwght, FontWeight.W300),
    Font(R.font.mulishvariablefontwght, FontWeight.Normal),
    Font(R.font.mulishvariablefontwght, FontWeight.Medium),
    Font(R.font.mulishvariablefontwght, FontWeight.SemiBold),
    Font(R.font.mulishvariablefontwght, FontWeight.Bold)
)

// ✍️ Typography styles
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Bold, // <-- SemiBold yerine Bold yaptık
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)
