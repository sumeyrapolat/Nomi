package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.ui.theme.BackgroundLight
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailBottomSheet(
    contact: Contact?,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSaveClick: (Contact) -> Unit,
    onChangePhotoClick: () -> Unit,
    onEditClick: (Contact) -> Unit,
    onDeleteConfirmed: (Contact) -> Unit
) {
    if (!isVisible || contact == null) return

    var firstName by remember { mutableStateOf(contact.firstName) }
    var lastName by remember { mutableStateOf(contact.lastName) }
    var phoneNumber by remember { mutableStateOf(contact.phoneNumber) }

    val isReadyToSave = firstName.isNotBlank() && phoneNumber.isNotBlank()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showDeleteSheet by remember { mutableStateOf(false) }


    // Menü durumu
    var menuExpanded by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BackgroundLight,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // === ÜST SAĞ MENÜ (Üç Nokta + Dropdown) ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.Gray
                    )
                }
                // 🔹 Burada ContactMenu bileşenini çağırıyoruz
                ContactMenu(
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    onEditClick = { onEditClick(contact) },
                    onDeleteClick = {
                        menuExpanded = false
                        showDeleteSheet = true // 👈 Delete'e tıklanınca sheet açılır
                    }                )
            }

            // === Profil Görseli ===
            Image(
                painter = painterResource(id = R.drawable.ic_contact),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(12.dp))

            // === Fotoğrafı Değiştir ===
            Text(
                text = "Change Photo",
                color = PrimaryBlue,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.clickable(onClick = onChangePhotoClick)
            )

            Spacer(Modifier.height(32.dp))

            // === Bilgiler ===
            ReadOnlyOutlinedBox(label = "First Name", value = firstName)
            Spacer(Modifier.height(16.dp))
            ReadOnlyOutlinedBox(label = "Last Name", value = lastName)
            Spacer(Modifier.height(16.dp))
            ReadOnlyOutlinedBox(label = "Phone Number", value = phoneNumber)

            Spacer(Modifier.height(32.dp))

            // === Kayıt Butonu ===
            SaveToPhoneContactButton(
                enabled = isReadyToSave,
                onClick = {
                    val updatedContact = contact.copy(
                        firstName = firstName,
                        lastName = lastName,
                        phoneNumber = phoneNumber
                    )
                    onSaveClick(updatedContact)
                }
            )

            Spacer(Modifier.height(220.dp))
        }


        // 👇 DELETE CONFIRM SHEET
        DeleteContactBottomSheet(
            isVisible = showDeleteSheet,
            onDismiss = { showDeleteSheet = false },
            onConfirm = {
                showDeleteSheet = false
                onDeleteConfirmed(contact) // 👈 dışarı callback ile bildir
            }
        )
    }
}
