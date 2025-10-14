package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.domain.Contact
import com.sumeyrapolat.nomi.ui.theme.BackgroundLight
import com.sumeyrapolat.nomi.ui.theme.Gray50
import com.sumeyrapolat.nomi.ui.theme.NomiTheme



@Composable
fun ContactListSection(
    initial: String, // "A", "B" gibi baş harf
    contacts: List<Contact> // O harfle başlayan kişiler
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

            // 🔥 Grup başlığı artık kartın içinde
            ContactGroupHeader(
                letter = initial
            )

            contacts.forEachIndexed { index, contact ->

                ContactListItem(
                    avatarResId = contact.avatarResId,
                    initial = contact.name.first().toString(),
                    name = contact.name,
                    phoneNumber = contact.phone
                )

                if (index < contacts.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        thickness = DividerDefaults.Thickness,
                        color = Gray50
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, name = "Contact List Section Preview")
@Composable
fun ContactListSectionPreview() {
    NomiTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Arka plan rengi
        ) {
            ContactListSection(
                initial = "A",
                contacts = listOf(
                    Contact(1, "Alice Wellington", "+1234567890", R.drawable.ic_contact),
                    Contact(2, "Amelia", "+1234567890") // Avatar olmayan bir örnek
                )
            )

            ContactListSection(
                initial = "B",
                contacts = listOf(
                    Contact(3, "Benjamin", "+1234567890", R.drawable.ic_contact),
                    Contact(4, "Bianca", "+1234567890"),
                    Contact(5, "Brandon", "+1234567890")
                )
            )
        }
    }
}