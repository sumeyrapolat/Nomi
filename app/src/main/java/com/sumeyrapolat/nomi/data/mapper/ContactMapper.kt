package com.sumeyrapolat.nomi.data.mapper

import com.sumeyrapolat.nomi.data.remote.dto.ContactDto
import com.sumeyrapolat.nomi.data.remote.dto.UserResponse
import com.sumeyrapolat.nomi.domain.model.Contact

fun ContactDto.toDomain(): Contact = Contact(
    id = "",
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    profileImageUrl = profileImageUrl
)

fun Contact.toDto(): ContactDto = ContactDto(
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    profileImageUrl = profileImageUrl
)

fun UserResponse.toDomain(): Contact {
    return Contact(
        id = id,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl
    )
}
