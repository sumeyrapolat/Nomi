package com.sumeyrapolat.nomi.domain.usecase


import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.domain.repository.ContactRepository
import javax.inject.Inject

class UpdateContactUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(contact: Contact) {
        repository.updateContact(contact)
    }
}
