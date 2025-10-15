package com.sumeyrapolat.nomi.presentation.contacts

import com.sumeyrapolat.nomi.domain.model.Contact

data class ContactsUiState(
    val contacts: List<Contact> = emptyList(),
    val isLoading: Boolean = false,
    val isContactCreated: Boolean = false,
    val toastMessage: String? = null, // 👈 yeni alan
    val error: String? = null
)
