package com.sumeyrapolat.nomi.presentation.components


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.ui.theme.DeleteRed
import com.sumeyrapolat.nomi.ui.theme.PrimaryBlue

@Composable
fun SwipeableContactRow(
    name: String,
    phoneNumber: String,
    avatarResId: Int? = null,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val maxSwipe = 160f // iki butonun toplam genişliği

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // 🔹 Arka plan (edit + delete)
        Row(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight()
                    .background(PrimaryBlue)
                    .clickable {
                        onEditClick()
                        scope.launch { offsetX.animateTo(0f, tween(250)) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight()
                    .background(DeleteRed)
                    .clickable {
                        onDeleteClick()
                        scope.launch { offsetX.animateTo(0f, tween(250)) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // 🔸 Ön plan (ContactListItem)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = offsetX.value.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffset = (offsetX.value + dragAmount).coerceIn(-maxSwipe, 0f)
                            scope.launch { offsetX.snapTo(newOffset) }
                        },
                        onDragEnd = {
                            // yarıyı geçtiyse tam aç, yoksa kapat
                            val shouldOpen = offsetX.value < -maxSwipe / 2
                            scope.launch {
                                offsetX.animateTo(if (shouldOpen) -maxSwipe else 0f, tween(200))
                            }
                        }
                    )
                },
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 0.dp
        ) {
            ContactListItem(
                avatarResId = avatarResId,
                name = name,
                phoneNumber = phoneNumber
            )
        }
    }
}
