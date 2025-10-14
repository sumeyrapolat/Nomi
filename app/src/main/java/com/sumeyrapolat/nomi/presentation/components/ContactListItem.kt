package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.ui.theme.Gray300
import com.sumeyrapolat.nomi.ui.theme.Gray900
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.NomiTheme
import com.sumeyrapolat.nomi.ui.theme.Typography

@Composable
fun ContactListItem(
    modifier: Modifier = Modifier,
    avatarResId: Int? = null, // Nullable yaptık
    name: String,
    phoneNumber: String,
    initial: String? = null // Baş harf de alabiliriz
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (avatarResId != null) {
            Image(
                painter = painterResource(id = avatarResId),
                contentDescription = "Contact Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Gray300)
            )
        } else if (initial != null) {
            // Eğer avatar yoksa baş harfi gösteren bir yer tutucu
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)), // Hafif renkli daire
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }


        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), // İsim kalın olsun
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

@Preview(showBackground = true)
@Composable
fun ContactListItemPreview() {
    NomiTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "A",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Gray950,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ContactListItem(
                avatarResId = R.drawable.ic_launcher_foreground, // Varsayılan bir avatar görseli kullanın
                name = "Alice Wellington",
                phoneNumber = "+1234567890"
            )
            Spacer(Modifier.height(8.dp))
            ContactListItem(
                avatarResId = R.drawable.ic_launcher_foreground,
                name = "Ali Can",
                phoneNumber = "+905551234567"
            )
        }
    }
}