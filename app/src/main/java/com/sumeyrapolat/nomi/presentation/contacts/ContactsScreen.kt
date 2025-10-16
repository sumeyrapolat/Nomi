package com.sumeyrapolat.nomi.presentation.contacts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.presentation.components.*
import com.sumeyrapolat.nomi.ui.theme.Gray100
import com.sumeyrapolat.nomi.R


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
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

    var isEditSheetVisible by remember { mutableStateOf(false) }
    var editingContact by remember { mutableStateOf<Contact?>(null) }

    val focusManager = LocalFocusManager.current
    val searchQuery = uiState.searchQuery
    val isSearchFocused = uiState.searchFocused
    val searchResults = uiState.filtered

    Scaffold(containerColor = Gray100) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (isSearchFocused) {
                        focusManager.clearFocus()
                        viewModel.onEvent(ContactEvent.SearchFocusChanged(false))
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                ContactsTopBar(onAddClick = { isAddSheetVisible = true })
                Spacer(Modifier.height(10.dp))

                SearchBar(
                    query = searchQuery,
                    onQueryChange = { query ->
                        viewModel.onEvent(ContactEvent.SearchQueryChanged(query))
                    },
                    onFocusChanged = { focused ->
                        viewModel.onEvent(ContactEvent.SearchFocusChanged(focused))
                    },
                    onSearchAction = {
                        viewModel.submitSearch()
                        viewModel.onEvent(ContactEvent.SearchFocusChanged(false))
                        focusManager.clearFocus()
                    }
                )

                Spacer(Modifier.height(8.dp))

                when {
                    searchQuery.isNotBlank() -> {
                        if (searchResults.isEmpty()) {
                            Spacer(Modifier.height(120.dp))
                            NoResultsState()
                        } else {
                            SearchResultsSection(
                                contacts = searchResults,
                                onContactClick = { contact ->
                                    selectedContact = contact
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }

                    isSearchFocused -> {
                        RecentSearchesSection(
                            recentSearches = uiState.recentSearches,
                            onClearAll = { viewModel.onEvent(ContactEvent.SearchClearAll) },
                            onRemoveItem = { query -> viewModel.onEvent(ContactEvent.SearchRemoveHistory(query)) },
                            onSearchClick = { selected ->
                                viewModel.onEvent(ContactEvent.SearchHistoryClick(selected))
                                focusManager.clearFocus()
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
                onToastTriggered = { messageKey ->
                    viewModel.showToast(messageKey)
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

            uiState.toastMessage?.let { messageKey ->
                val messageText = when (messageKey) {
                    "contact_added_message" -> stringResource(id = R.string.contact_added_message)
                    "contact_updated_message" -> stringResource(id = R.string.contact_updated_message)
                    "contact_deleted_message" -> stringResource(id = R.string.contact_deleted_message)
                    else -> null
                }

                messageText?.let {
                    ToastMessage(
                        message = it,
                        type = ToastType.SUCCESS,
                        onDismiss = { viewModel.resetToast() }
                    )
                }
            }



        }
    }
}
