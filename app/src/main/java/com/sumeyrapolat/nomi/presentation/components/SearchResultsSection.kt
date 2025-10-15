package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.ui.theme.BackgroundLight
import com.sumeyrapolat.nomi.ui.theme.Gray300
import com.sumeyrapolat.nomi.ui.theme.Gray50
import com.sumeyrapolat.nomi.ui.theme.Gray400
import com.sumeyrapolat.nomi.ui.theme.NomiTheme
import com.sumeyrapolat.nomi.ui.theme.Typography

@Composable
fun SearchResultsSection(
    title: String = stringResource(id = R.string.top_name_matches),
    contacts: List<Contact>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundLight
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // 🔹 Bölüm başlığı
            Text(
                text = title.uppercase(),
                style = Typography.titleMedium.copy(
                    color = Gray300,
                ),
                color = Gray300,
                modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 8.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 10.dp),
                thickness = DividerDefaults.Thickness,
                color = Gray50
            )

            // 🔹 Arama sonucu kişi listesi
            contacts.forEachIndexed { index, contact ->

                ContactListItem(
                    firstName = contact.firstName,
                    lastName = contact.lastName,
                    phoneNumber = contact.phoneNumber,
                    profileImageUrl = contact.profileImageUrl,
                    onClick = {}

                )
                if (index < contacts.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        thickness = DividerDefaults.Thickness,
                        color = Gray50
                    )
                }
            }
        }
    }
}