package com.sumeyrapolat.nomi.presentation.contacts

import com.sumeyrapolat.nomi.domain.model.Contact

sealed class ContactEvent {
    object LoadContacts : ContactEvent()

    data class AddContact(val firstName: String, val lastName: String, val phone: String) :
        ContactEvent()

    data class DeleteContact(val contact: Contact) : ContactEvent()
    data class UpdateContact(val contact: Contact) : ContactEvent()

    // 🔍 Search
    data class SearchQueryChanged(val query: String) : ContactEvent()
    data class SearchFocusChanged(val focused: Boolean) : ContactEvent()
    data class SearchHistoryClick(val query: String) : ContactEvent()
    object SearchClearAll : ContactEvent()
    data class SearchRemoveHistory(val query: String) : ContactEvent()
}
