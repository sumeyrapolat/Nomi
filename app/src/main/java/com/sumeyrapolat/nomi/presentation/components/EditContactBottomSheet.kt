package com.sumeyrapolat.nomi.presentation.components

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.ui.theme.BackgroundLight
import com.sumeyrapolat.nomi.ui.theme.Gray300
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactBottomSheet(
    isVisible: Boolean,
    contact: Contact?,
    onDismiss: () -> Unit,
    onSave: (Contact) -> Unit
) {
    if (!isVisible || contact == null) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    var firstName by remember { mutableStateOf(contact.firstName) }
    var lastName by remember { mutableStateOf(contact.lastName) }
    var phone by remember { mutableStateOf(contact.phoneNumber) }
    var photoUri by remember { mutableStateOf(contact.profileImageUrl?.let { Uri.parse(it) }) }

    var showPhotoPicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // --- Kamera ve galeri launcher'ları ---
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val uri = saveBitmapToCache(context, it)
            photoUri = uri
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { photoUri = it }
    }

    // --- Kamera izni launcher'ı ---
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show()
        }
    }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = BackgroundLight,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = { BottomSheetDefaults.DragHandle() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // === Top Bar ===
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cancel",
                        color = PrimaryBlue,
                        fontSize = 13.sp,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.clickable {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                onDismiss()
                            }
                        }
                    )

                    Text(
                        text = "Edit Contact",
                        style = MaterialTheme.typography.labelLarge,
                        color = Gray950,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 13.sp,
                        color = if (firstName.isNotBlank() && phone.isNotBlank()) PrimaryBlue else Color.Gray,
                        modifier = Modifier.clickable(
                            enabled = firstName.isNotBlank() && phone.isNotBlank()
                        ) {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                val updated = contact.copy(
                                    firstName = firstName,
                                    lastName = lastName,
                                    phoneNumber = phone,
                                    profileImageUrl = photoUri?.toString()
                                )
                                onSave(updated)
                            }
                        }
                    )
                }

                Spacer(Modifier.height(8.dp))

                // === Profil Görseli ===
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Gray300),
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(photoUri),
                            contentDescription = "Selected Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_contact),
                            contentDescription = "Default Avatar",
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Change Photo",
                    color = PrimaryBlue,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable {
                        showPhotoPicker = true
                    }
                )

                Spacer(Modifier.height(24.dp))

                // === Text Fields ===
                ContactTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = "First Name"
                )

                Spacer(Modifier.height(12.dp))

                ContactTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = "Last Name"
                )

                Spacer(Modifier.height(12.dp))

                ContactTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Phone Number",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(Modifier.height(360.dp))
            }
        }
    }

    // --- Fotoğraf seçici alt sheet ---
    if (showPhotoPicker) {
        PhotoPickerBottomSheet(
            onCameraClick = {
                val permission = android.Manifest.permission.CAMERA
                when {
                    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                        cameraLauncher.launch(null)
                    }
                    else -> {
                        cameraPermissionLauncher.launch(permission)
                    }
                }
                showPhotoPicker = false
            },
            onGalleryClick = {
                galleryLauncher.launch("image/*")
                showPhotoPicker = false
            },
            onCancelClick = {
                showPhotoPicker = false
            }
        )
    }
}
