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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.presentation.contacts.ContactsViewModel
import com.sumeyrapolat.nomi.ui.theme.BackgroundLight
import com.sumeyrapolat.nomi.ui.theme.Gray300
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewContactBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val viewModel: ContactsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // --- 1. Kamera ve galeri launcher'ları ---
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val uri = saveBitmapToCache(context, it)
            viewModel.onPhotoSelected(uri)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onPhotoSelected(it) }
    }

    // --- 2. Kamera izni launcher'ı ---
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show()
        }
    }

    // --- 3. Bottom Sheet ---
    if (isVisible) {
        LaunchedEffect(Unit) {
            firstName = ""
            lastName = ""
            phone = ""
            viewModel.clearSelectedPhoto()
        }

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = BackgroundLight,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = null,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
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
                        text = stringResource(id = R.string.contact_cancel),
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
                        text = stringResource(id = R.string.contact_new_title),
                        style = MaterialTheme.typography.labelLarge,
                        color = Gray950,
                        fontSize = 16.sp
                    )

                    Text(
                        text = stringResource(id = R.string.contact_done),
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 13.sp,
                        color = if (firstName.isNotBlank() && phone.isNotBlank()) PrimaryBlue else Color.Gray,
                        modifier = Modifier.clickable(
                            enabled = firstName.isNotBlank() && phone.isNotBlank()
                        ) {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                onSave(firstName, lastName, phone)
                            }
                        }
                    )
                }

                Spacer(Modifier.height(8.dp))

                // === Avatar ===
                val selectedImageUri = uiState.selectedImageUri
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Gray300),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "Selected Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_contact),
                            contentDescription = "Default Avatar",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.contact_add_photo),
                    color = PrimaryBlue,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable {
                        showBottomSheet = true
                    }
                )

                Spacer(Modifier.height(32.dp))

                // === Text Fields ===
                ContactTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = stringResource(id = R.string.contact_first_name)
                )

                Spacer(Modifier.height(12.dp))

                ContactTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = stringResource(id = R.string.contact_last_name)
                )

                Spacer(Modifier.height(12.dp))

                ContactTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = stringResource(id = R.string.contact_phone_number),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(150.dp))
            }
        }
    }

    if (showBottomSheet) {
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
                showBottomSheet = false
            },
            onGalleryClick = {
                galleryLauncher.launch("image/*")
                showBottomSheet = false
            },
            onCancelClick = {
                showBottomSheet = false
            }
        )
    }
}

// --- Yardımcı fonksiyon ---
fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
    file.outputStream().use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}
