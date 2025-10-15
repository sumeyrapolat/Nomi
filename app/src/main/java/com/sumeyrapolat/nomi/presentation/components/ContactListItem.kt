package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.ui.theme.Blue50
import com.sumeyrapolat.nomi.ui.theme.Blue500
import com.sumeyrapolat.nomi.ui.theme.Gray300
import com.sumeyrapolat.nomi.ui.theme.Gray900
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.NomiTheme
import com.sumeyrapolat.nomi.ui.theme.PrimaryBlue

@Composable
fun ContactListItem(
    modifier: Modifier = Modifier,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    profileImageUrl: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar varsa göster, yoksa ilk harf
        if (!profileImageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = profileImageUrl,
                contentDescription = "Contact Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Gray300)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Blue50),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = firstName.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "$firstName $lastName",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Gray950
            )
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.bodyMedium,
                color = Gray900
            )
        }
    }
}
