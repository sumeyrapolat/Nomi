package com.sumeyrapolat.nomi.presentation.contacts

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumeyrapolat.nomi.data.RecentSearchManager
import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.domain.usecase.AddContactUseCase
import com.sumeyrapolat.nomi.domain.usecase.DeleteContactUseCase
import com.sumeyrapolat.nomi.domain.usecase.GetContactsUseCase
import com.sumeyrapolat.nomi.domain.usecase.UpdateContactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val addContactUseCase: AddContactUseCase,
    private val deleteContactUseCase: DeleteContactUseCase,
    private val updateContactUseCase: UpdateContactUseCase,
    private val recentSearchManager: RecentSearchManager

) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // recent searches dinle
        viewModelScope.launch {
            recentSearchManager.recentSearches.collect { list ->
                _uiState.update { it.copy(recentSearches = list) }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.LoadContacts -> loadContacts()
            is ContactEvent.AddContact -> addContact(event.firstName, event.lastName, event.phone)
            is ContactEvent.DeleteContact -> deleteContact(event.contact)
            is ContactEvent.UpdateContact -> updateContact(event.contact)

            // 🔍 Search events
            is ContactEvent.SearchQueryChanged -> onSearchChanged(event.query)
            is ContactEvent.SearchFocusChanged -> _uiState.update { it.copy(searchFocused = event.focused) }
            is ContactEvent.SearchHistoryClick -> applySearchFromHistory(event.query)
            is ContactEvent.SearchClearAll -> clearAllHistory()
            is ContactEvent.SearchRemoveHistory -> removeFromHistory(event.query)
        }
    }

    private fun onSearchChanged(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun applySearchFromHistory(query: String) {
        _uiState.update { it.copy(searchQuery = query, searchFocused = false) }
        viewModelScope.launch { recentSearchManager.add(query) }
    }

    private fun clearAllHistory() {
        viewModelScope.launch { recentSearchManager.clear() }
    }

    private fun removeFromHistory(q: String) {
        viewModelScope.launch { recentSearchManager.remove(q) }
    }

    /** dışarıdan çağır: kullanıcı klavyeden "Search/Done" bastığında  */
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun submitSearch() {
        val q = _uiState.value.searchQuery
        viewModelScope.launch { recentSearchManager.add(q) }
    }

    // 🔹 Rehberi yükle
    private fun loadContacts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val contacts = getContactsUseCase()
                _uiState.update { it.copy(contacts = contacts, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    // 🔹 Yeni kişi ekle
    private fun addContact(firstName: String, lastName: String, phone: String) {
        viewModelScope.launch {
            try {
                val imageUri = _uiState.value.selectedImageUri?.toString()

                addContactUseCase(
                    Contact(
                        firstName = firstName,
                        lastName = lastName,
                        phoneNumber = phone,
                        profileImageUrl = imageUri
                    )
                )

                _uiState.update { it.copy(
                    isContactCreated = true,
                    selectedImageUri = null,
                    toastMessage = "contact_added_message"
                ) }

                loadContacts()

                delay(2000)
                _uiState.update { it.copy(isContactCreated = false) }

            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    // 🔹 Kişi sil
    private fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            try {
                deleteContactUseCase(contact.id)
                loadContacts()
                _uiState.update {
                    it.copy(toastMessage = "contact_deleted_message")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    // 🔹 Kişi güncelle (Edit)
    private fun updateContact(contact: Contact) {
        viewModelScope.launch {
            try {
                updateContactUseCase(contact)
                loadContacts()
                _uiState.update { it.copy(toastMessage = "contact_updated_message") }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    // 🔹 Toast mesajını sıfırla
    fun resetToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    // 🔹 Fotoğraf seçimi
    fun onPhotoSelected(uri: Uri) {
        _uiState.update { it.copy(selectedImageUri = uri) }
    }

    fun clearSelectedPhoto() {
        _uiState.update { it.copy(selectedImageUri = null) }
    }
}
