package com.sumeyrapolat.nomi.data.repository

import com.sumeyrapolat.nomi.data.mapper.toDomain
import com.sumeyrapolat.nomi.data.remote.ContactApi
import com.sumeyrapolat.nomi.data.remote.dto.CreateUserRequest
import com.sumeyrapolat.nomi.domain.model.Contact
import com.sumeyrapolat.nomi.domain.repository.ContactRepository

class ContactRepositoryImpl(
    private val api: ContactApi
) : ContactRepository {

    private val apiKey = "6143a134-50c5-48bf-937d-feb635f3931d"

    override suspend fun getAllContacts(): List<Contact> {
        val response = api.getAllUsers(apiKey)
        if (response.isSuccessful) {
            val body = response.body()
            val users = body?.data?.users ?: emptyList()
            return users.map { it.toDomain() }
        } else {
            throw Exception("Failed to load contacts: ${response.code()}")
        }
    }


    override suspend fun addContact(contact: Contact) {
        val request = CreateUserRequest(
            firstName = contact.firstName,
            lastName = contact.lastName,
            phoneNumber = contact.phoneNumber,
            profileImageUrl = contact.profileImageUrl
        )

        val response = api.createUser(apiKey, request)
        if (!response.isSuccessful) {
            throw Exception("Failed to add contact: ${response.code()}")
        }
    }

    override suspend fun deleteContact(id: String) { // 🔹 String
        val response = api.deleteUser(apiKey, id)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete contact: ${response.code()}")
        }
    }

}
