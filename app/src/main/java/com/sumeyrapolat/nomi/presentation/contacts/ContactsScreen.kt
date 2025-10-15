package com.sumeyrapolat.nomi.presentation.contacts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.presentation.components.*
import com.sumeyrapolat.nomi.ui.theme.Gray100

@Composable
fun ContactsScreen() {

    val viewModel: ContactsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    var showDeleteSheet by remember { mutableStateOf(false) }
    var contactToDelete by remember { mutableStateOf<Contact?>(null) }

    // Ekran ilk açıldığında kullanıcıları yükle
    LaunchedEffect(Unit) {
        viewModel.onEvent(ContactEvent.LoadContacts)
    }

    var isAddSheetVisible by remember { mutableStateOf(false) }
    var selectedContact by remember { mutableStateOf<Contact?>(null) }

    Scaffold(containerColor = Gray100) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 🔹 Üst Bar
            ContactsTopBar(onAddClick = { isAddSheetVisible = true })
            Spacer(Modifier.height(10.dp))

            // 🔹 Arama Alanı
            SearchBar(onSearch = {})

            when {
                uiState.isLoading -> {
                    Spacer(Modifier.height(120.dp))
                    LoadingState()
                }

                uiState.contacts.isEmpty() -> {
                    Spacer(Modifier.height(120.dp))
                    EmptyContactsState(onCreateClick = { isAddSheetVisible = true })
                }

                else -> {
                    LazyColumn {
                        uiState.contacts
                            .groupBy { it.firstName.firstOrNull()?.uppercaseChar() ?: '#' }
                            .toSortedMap()
                            .forEach { (initial, group) ->
                                item {
                                    ContactListSection(
                                        initial = initial.toString(),
                                        contacts = group,
                                        onContactClick = { contact ->
                                            selectedContact = contact
                                        },
                                        onDeleteClick = { contact ->
                                            // 🔹 Liste üzerinden silme
                                            contactToDelete = contact
                                            showDeleteSheet = true
                                        },
                                        onEditClick = { contact ->
                                            // 🔹 Edit için ileride kullanılacak
                                            selectedContact = contact
                                        }
                                    )
                                }
                            }
                    }
                }
            }
        }

        // 🔹 Profil (detay) bottom sheet
        ContactDetailBottomSheet(
            contact = selectedContact,
            isVisible = selectedContact != null,
            onDismiss = { selectedContact = null },
            onSaveClick = {
                // TODO: save to phone contact (yerel rehbere kaydetme)
            },
            onEditClick = { updatedContact ->
                viewModel.onEvent(ContactEvent.UpdateContact(updatedContact)) // 👈 burada tetikleniyor!
            },
            onDeleteConfirmed = { contact ->
                viewModel.onEvent(ContactEvent.DeleteContact(contact))
            }
        )

        // 🔹 Yeni kişi ekleme bottom sheet
        AddNewContactBottomSheet(
            isVisible = isAddSheetVisible,
            onDismiss = { isAddSheetVisible = false },
            onSave = { firstName, lastName, phone ->
                // ✅ AddContact Event’i ViewModel’e gönder
                viewModel.onEvent(
                    ContactEvent.AddContact(
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone
                    )
                )
                isAddSheetVisible = false
            }
        )

        // 🔹 Silme onay bottom sheet (liste içinden çağrıldığında)
        DeleteContactBottomSheet(
            isVisible = showDeleteSheet,
            onDismiss = { showDeleteSheet = false },
            onConfirm = {
                contactToDelete?.let { contact ->
                    viewModel.onEvent(ContactEvent.DeleteContact(contact))
                    contactToDelete = null
                }
                showDeleteSheet = false
            }
        )

        uiState.toastMessage?.let { message ->
            ToastMessage(
                type = ToastType.SUCCESS,
                onDismiss = {
                    viewModel.resetToast()
                }
            )
        }


    }
}
