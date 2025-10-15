package com.sumeyrapolat.nomi.presentation.contacts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.domain.usecase.AddContactUseCase
import com.sumeyrapolat.nomi.domain.usecase.DeleteContactUseCase
import com.sumeyrapolat.nomi.domain.usecase.GetContactsUseCase
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
    private val deleteContactUseCase: DeleteContactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.LoadContacts -> loadContacts()
            is ContactEvent.AddContact -> addContact(event.firstName, event.lastName, event.phone)
            is ContactEvent.DeleteContact -> deleteContact(event.contact)
        }
    }

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

    private fun addContact(firstName: String, lastName: String, phone: String) {
        viewModelScope.launch {
            try {
                addContactUseCase(Contact(firstName = firstName, lastName = lastName, phoneNumber = phone))
                _uiState.update { it.copy(isContactCreated = true) }

                loadContacts()

                delay(2000)
                _uiState.update { it.copy(isContactCreated = false) }

            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    private fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            try {
                deleteContactUseCase(contact.id)
                loadContacts()
                // 👇 Kullanıcıya toast mesajı göstermek için state'i güncelle
                _uiState.update {
                    it.copy(toastMessage = "contact_deleted_message") // sadece key veya sabit işaret
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun resetToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

}
