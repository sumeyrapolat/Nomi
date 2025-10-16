package com.sumeyrapolat.nomi.presentation.components

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.ui.theme.BackgroundLight
import com.sumeyrapolat.nomi.ui.theme.PrimaryBlue
import androidx.core.content.ContextCompat
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.util.saveContactToPhone
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailBottomSheet(
    contact: Contact?,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSaveClick: (Contact) -> Unit,
    onEditClick: (Contact) -> Unit,
    onToastTriggered: (String) -> Unit,
    onDeleteConfirmed: (Contact) -> Unit
) {
    if (!isVisible || contact == null) return

    var firstName by remember { mutableStateOf(contact.firstName) }
    var lastName by remember { mutableStateOf(contact.lastName) }
    var phoneNumber by remember { mutableStateOf(contact.phoneNumber) }
    var profileImage by remember { mutableStateOf(contact.profileImageUrl) }
    var isContactSaved by remember(contact.id) { mutableStateOf(contact.isSaved) }

    val isReadyToSave = firstName.isNotBlank() && phoneNumber.isNotBlank()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showDeleteSheet by remember { mutableStateOf(false) }
    var showPhotoPicker by remember { mutableStateOf(false) }

    var menuExpanded by remember { mutableStateOf(false) }
    var isEditSheetVisible by remember { mutableStateOf(false) }
    var editingContact by remember { mutableStateOf<Contact?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // --- Kamera ve Galeri launcher'ları ---
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val uri = saveBitmapToCache(context, it)
            profileImage = uri.toString()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { profileImage = it.toString() }
    }


    // --- Kamera izni ---
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    val contactPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            coroutineScope.launch {
                val updatedContact = contact.copy(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    profileImageUrl = profileImage
                )
                saveContactToPhone(context, updatedContact)
                onToastTriggered("contact_added_message")
                isContactSaved = true
            }
        } else {
        }
    }



    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BackgroundLight,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null,
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // === Üst Menü ===
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
                        tint = Gray950
                    )
                }
                ContactMenu(
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    onEditClick = {
                        menuExpanded = false
                        editingContact = contact
                        isEditSheetVisible = true
                    },
                    onDeleteClick = {
                        menuExpanded = false
                        showDeleteSheet = true
                    }
                )
            }

            // === Profil Görseli ===
            ProfileImageWithDiffuseGlow(imageUrl = profileImage)

            Spacer(Modifier.height(12.dp))

            // === Fotoğrafı Değiştir ===
            Text(
                text = "Change Photo",
                color = PrimaryBlue,
                fontSize = 14.sp,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.clickable { showPhotoPicker = true } // 👈 alt sheet açılır
            )

            Spacer(Modifier.height(32.dp))

            // === Bilgiler ===
            ReadOnlyOutlinedBox(label = "First Name", value = firstName)
            Spacer(Modifier.height(16.dp))
            ReadOnlyOutlinedBox(label = "Last Name", value = lastName)
            Spacer(Modifier.height(16.dp))
            ReadOnlyOutlinedBox(label = "Phone Number", value = phoneNumber)

            Spacer(Modifier.height(50.dp))


            SaveToPhoneContactButton(
                enabled = isReadyToSave,
                isContactAlreadySaved = isContactSaved,
                onClick = {
                    val updatedContact = contact.copy(
                        firstName = firstName,
                        lastName = lastName,
                        phoneNumber = phoneNumber,
                        profileImageUrl = profileImage
                    )

                    val permission = android.Manifest.permission.WRITE_CONTACTS
                    when {
                        // ✅ Eğer izin zaten varsa:
                        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                            coroutineScope.launch {
                                saveContactToPhone(context, updatedContact)
                                onToastTriggered("contact_added_message")
                                isContactSaved = true
                            }
                        }

                        // ✅ Eğer izin yoksa, iste:
                        else -> contactPermissionLauncher.launch(permission)
                    }

                    // 🔹 App içinde kaydetme işlemi (örneğin veritabanına)
                    onSaveClick(updatedContact)
                }
            )

            Spacer(Modifier.height(30.dp))


        }

        // === Delete Confirm Sheet ===
        DeleteContactBottomSheet(
            isVisible = showDeleteSheet,
            onDismiss = { showDeleteSheet = false },
            onConfirm = {
                showDeleteSheet = false
                onDeleteConfirmed(contact)
            }
        )

        // === Edit Sheet ===
        if (isEditSheetVisible && editingContact != null) {
            EditContactBottomSheet(
                isVisible = isEditSheetVisible,
                contact = editingContact,
                onDismiss = { isEditSheetVisible = false },
                onSave = { updated ->
                    onEditClick(updated)
                    isEditSheetVisible = false
                }
            )
        }
    }

    // === Fotoğraf Seçici Alt Sheet ===
    if (showPhotoPicker) {
        PhotoPickerBottomSheet(
            onCameraClick = {
                val permission = android.Manifest.permission.CAMERA
                when {
                    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                        cameraLauncher.launch(null)
                    }
                    else -> cameraPermissionLauncher.launch(permission)
                }
                showPhotoPicker = false
            },
            onGalleryClick = {
                galleryLauncher.launch("image/*")
                showPhotoPicker = false
            },
            onCancelClick = { showPhotoPicker = false }
        )
    }
}
