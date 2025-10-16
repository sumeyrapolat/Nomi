package com.sumeyrapolat.nomi.presentation.contacts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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

    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }

    val viewModel: ContactsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    var showDeleteSheet by remember { mutableStateOf(false) }
    var contactToDelete by remember { mutableStateOf<Contact?>(null) }

    // Ekran ilk açıldığında kullanıcıları yükle
    LaunchedEffect(Unit) {
        viewModel.onEvent(ContactEvent.LoadContacts)
        println("🔍 Güncel arama geçmişi: ${uiState.recentSearches}")

    }

    var isAddSheetVisible by remember { mutableStateOf(false) }
    var selectedContact by remember { mutableStateOf<Contact?>(null) }

    val filteredContacts = remember(searchQuery, uiState.contacts) {
        if (searchQuery.isBlank()) emptyList()
        else uiState.contacts.filter { contact ->
            val query = searchQuery.trim().lowercase()
            // 👇 sadece adının ilk harfi eşleşirse göster
            contact.firstName.firstOrNull()?.lowercaseChar() == query.firstOrNull()
        }
    }

    var isEditSheetVisible by remember { mutableStateOf(false) }
    var editingContact by remember { mutableStateOf<Contact?>(null) }


    Scaffold(containerColor = Gray100) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (isSearchFocused) isSearchFocused = false
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // 🔹 Üst Bar
                ContactsTopBar(onAddClick = { isAddSheetVisible = true })
                Spacer(Modifier.height(10.dp))

                // 🔹 SearchBarArama Alanı
                SearchBar(
                    onSearch = { query -> searchQuery = query },
                    onFocusClick = { isSearchFocused = true } // sadece tıklandığında aktifleşsin
                )

                Spacer(Modifier.height(8.dp))

                when {
                    // 🔹 Arama yazılıysa sonuçları göster
                    searchQuery.isNotBlank() -> {
                        SearchResultsSection(contacts = filteredContacts)
                    }

                    // 🔹 Sadece focus varsa (arama yok ama tıklanmış)
                    isSearchFocused -> {
                        RecentSearchesSection(
                            recentSearches = uiState.recentSearches,
                            onClearAll = { viewModel.onEvent(ContactEvent.SearchClearAll) },
                            onRemoveItem = { query -> viewModel.onEvent(ContactEvent.SearchRemoveHistory(query)) },
                            onSearchClick = { selected ->
                                searchQuery = selected
                                isSearchFocused = false
                            }
                        )
                    }

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
                                            onContactClick = { selectedContact = it },
                                            onDeleteClick = {
                                                contactToDelete = it
                                                showDeleteSheet = true
                                            },
                                            onEditClick = {
                                                editingContact = it
                                                isEditSheetVisible = true
                                            }
                                        )
                                    }
                                }
                        }
                    }
                }
            }

            EditContactBottomSheet(
                isVisible = isEditSheetVisible,
                contact = editingContact,
                onDismiss = {
                    isEditSheetVisible = false
                    editingContact = null
                },
                onSave = { updatedContact ->
                    viewModel.onEvent(ContactEvent.UpdateContact(updatedContact))
                    isEditSheetVisible = false
                    editingContact = null
                }
            )


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
}
