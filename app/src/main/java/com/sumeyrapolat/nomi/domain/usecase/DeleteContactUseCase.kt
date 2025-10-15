package com.sumeyrapolat.nomi.domain.usecase

import com.sumeyrapolat.nomi.domain.repository.ContactRepository
import javax.inject.Inject

class DeleteContactUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(id: String) { // 🔹 Int değil String
        repository.deleteContact(id)
    }
}
