package com.sumeyrapolat.nomi.presentation.contacts

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val updateContactUseCase: UpdateContactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState = _uiState.asStateFlow()

    // 👇 Event yönlendirmesi
    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.LoadContacts -> loadContacts()
            is ContactEvent.AddContact -> addContact(event.firstName, event.lastName, event.phone)
            is ContactEvent.DeleteContact -> deleteContact(event.contact)
            is ContactEvent.UpdateContact -> updateContact(event.contact) // ✅ eksik olan satır eklendi
        }
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

                _uiState.update { it.copy(isContactCreated = true, selectedImageUri = null) }

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
                _uiState.update { it.copy(toastMessage = "Contact updated successfully") }
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
