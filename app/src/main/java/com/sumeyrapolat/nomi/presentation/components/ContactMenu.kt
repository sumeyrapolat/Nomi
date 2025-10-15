package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.ui.theme.BackgroundDark
import com.sumeyrapolat.nomi.ui.theme.BackgroundLight
import com.sumeyrapolat.nomi.ui.theme.DeleteRed
import com.sumeyrapolat.nomi.ui.theme.Gray100

@Composable
fun ContactMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        offset = DpOffset(x = (-25).dp, y = (-20).dp),
        modifier = Modifier
            .width(170.dp)
            .background(color = BackgroundLight)
    ) {
        // --- Edit ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onDismiss()
                    onEditClick()
                }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Edit",
                    color = BackgroundDark,
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null,
                    tint = BackgroundDark
                )
            }
        }

        Divider(color = Gray100)

        // --- Delete ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onDismiss()
                    onDeleteClick()
                }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Delete",
                    color = DeleteRed,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    tint = DeleteRed
                )
            }
        }
    }
}

