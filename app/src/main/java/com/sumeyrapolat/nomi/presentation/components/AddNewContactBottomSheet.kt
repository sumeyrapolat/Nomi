package com.sumeyrapolat.nomi.presentation.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.ui.theme.Gray300
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.NomiTheme
import com.sumeyrapolat.nomi.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewContactBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var showBottomSheet by remember { mutableStateOf(false) }

    if (isVisible) {
        LaunchedEffect(Unit) {
            firstName = ""
            lastName = ""
            phone = ""
        }
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = { BottomSheetDefaults.DragHandle() },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
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
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Gray300),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_contact),
                        contentDescription = "Avatar Placeholder",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(60.dp)
                    )
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

                Spacer(Modifier.height(24.dp))

                // === Text Fields (component versiyonu) ===
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

                Spacer(Modifier.height(180.dp))
            }
        }
    }

    if (showBottomSheet) {
        PhotoPickerBottomSheet(
            onCameraClick = {
                // TODO: Kamera'yı açma kodunu buraya ekleyin
                showBottomSheet = false // İşlem seçildikten sonra sheet'i kapat
            },
            onGalleryClick = {
                // TODO: Galeri'yi açma kodunu buraya ekleyin
                showBottomSheet = false // İşlem seçildikten sonra sheet'i kapat
            },
            onCancelClick = {
                showBottomSheet = false // İptale basılınca sheet'i kapat
            }
        )
    }

}
