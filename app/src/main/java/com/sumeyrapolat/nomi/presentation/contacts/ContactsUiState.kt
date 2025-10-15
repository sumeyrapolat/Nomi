package com.sumeyrapolat.nomi.presentation.contacts

import android.net.Uri
import com.sumeyrapolat.nomi.domain.model.Contact

data class ContactsUiState(
    val contacts: List<Contact> = emptyList(),
    val isLoading: Boolean = false,
    val isContactCreated: Boolean = false,
    val toastMessage: String? = null,
    val error: String? = null,
    val selectedImageUri: Uri? = null
)
