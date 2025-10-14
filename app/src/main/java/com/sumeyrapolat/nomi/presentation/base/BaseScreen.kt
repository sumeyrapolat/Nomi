package com.sumeyrapolat.nomi.presentation.base

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.sumeyrapolat.nomi.ui.theme.Gray100

@Composable
fun BaseScreen(
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    topBar: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val view = LocalView.current
    val window = (view.context as? Activity)?.window

    WindowCompat.setDecorFitsSystemWindows(window!!, false)
    window.statusBarColor = backgroundColor.toArgb()
    window.navigationBarColor = backgroundColor.toArgb()

    Scaffold(
        topBar = { topBar?.invoke() },
        containerColor = Gray100
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            content(paddingValues)
        }
    }
}
