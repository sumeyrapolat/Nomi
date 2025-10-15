package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.ui.theme.BackgroundLight
import com.sumeyrapolat.nomi.ui.theme.Gray50

@Composable
fun ContactListSection(
    initial: String, // "A", "B" gibi baş harf
    contacts: List<Contact>, // O harfle başlayan kişiler
    onContactClick: (Contact) -> Unit, // kişi satırına tıklama
    onEditClick: (Contact) -> Unit = {}, // düzenle butonu
    onDeleteClick: (Contact) -> Unit = {} // sil butonu
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Grup başlığı
            ContactGroupHeader(letter = initial)

            // 🔹 Artık her kişi SwipeableContactRow oldu
            contacts.forEachIndexed { index, contact ->

                SwipeableContactRow(
                    firstName = contact.firstName,
                    lastName = contact.lastName,
                    phoneNumber = contact.phoneNumber,
                    profileImageUrl = contact.profileImageUrl,
                    onEditClick = { onEditClick(contact) },
                    onDeleteClick = { onDeleteClick(contact) },
                    onContactClick = { onContactClick(contact) } // 👈 kişi satırı tıklanabilir
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
