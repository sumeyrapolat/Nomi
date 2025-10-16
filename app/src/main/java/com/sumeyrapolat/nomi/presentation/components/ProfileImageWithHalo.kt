package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest

@Composable
fun ProfileImageWithDiffuseGlow(imageUrl: String?) {
    val context = LocalContext.current
    var dominantColor by remember { mutableStateOf(Color(0xFFE0E0E0)) }

    // Dominant rengi çıkar
    LaunchedEffect(imageUrl) {
        imageUrl?.let { url ->
            try {
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .allowHardware(false)
                    .build()
                val result = context.imageLoader.execute(request)
                val bitmap = result.drawable?.toBitmap()
                bitmap?.let { bmp ->
                    val palette = Palette.from(bmp).generate()
                    val colorInt = palette.getDominantColor(android.graphics.Color.LTGRAY)
                    dominantColor = Color(colorInt)
                }
            } catch (_: Exception) { }
        }
    }

    // 🔹 Daha doğal ve yayılmış aura
    Box(
        modifier = Modifier
            .size(95.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        dominantColor.copy(alpha = 0.3f),
                        dominantColor.copy(alpha = 0.2f),
                        dominantColor.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    center = Offset(120f, 100f),
                    radius = 1000f
                ),
                shape = RoundedCornerShape(120.dp) // 🔹 daha yumuşak geçiş
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = "Profile",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}
