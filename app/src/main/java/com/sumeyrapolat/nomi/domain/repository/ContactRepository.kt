package com.sumeyrapolat.nomi.domain.repository

import com.sumeyrapolat.nomi.domain.model.Contact


interface ContactRepository {
    suspend fun getAllContacts(): List<Contact>
    suspend fun addContact(contact: Contact)
    suspend fun deleteContact(id: String) // 🔹 String
    suspend fun updateContact(contact: Contact)

}
