package com.sumeyrapolat.nomi.presentation.contacts

import com.sumeyrapolat.nomi.domain.model.Contact

sealed class ContactEvent {
    object LoadContacts : ContactEvent()

    // Add
    data class AddContact(
        val firstName: String,
        val lastName: String,
        val phone: String
    ) : ContactEvent()

    // Delete
    data class DeleteContact(val contact: Contact) : ContactEvent()

    // ✅ Update (Edit)
    data class UpdateContact(val contact: Contact) : ContactEvent()
}
