package com.sumeyrapolat.nomi.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// 🎨 Dark Mode renk seti
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    secondary = Gray400,
    tertiary = SuccessGreen,
    background = Gray950,
    onBackground = Color.White,
    surface = Gray900,
    onSurface = Color.White,
    error = DeleteRed,
    onError = Color.White
)

// ☀️ Light Mode renk seti
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    secondary = Gray300,
    tertiary = SuccessGreen,
    background = Gray50,
    onBackground = Gray950,
    surface = Color.White,
    onSurface = Gray900,
    error = DeleteRed,
    onError = Color.White
)

@Composable
fun NomiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color Android 12+ için (isteğe bağlı)
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
