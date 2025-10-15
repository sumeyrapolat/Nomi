package com.sumeyrapolat.nomi.domain.usecase

import com.sumeyrapolat.nomi.domain.repository.ContactRepository

class GetContactsUseCase(
    private val repository: ContactRepository
) {
    suspend operator fun invoke() = repository.getAllContacts()
}