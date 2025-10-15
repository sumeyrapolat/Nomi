package com.sumeyrapolat.nomi.domain.usecase

import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.domain.repository.ContactRepository

class AddContactUseCase(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(contact: Contact) = repository.addContact(contact)
}
