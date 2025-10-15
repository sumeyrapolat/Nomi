package com.sumeyrapolat.nomi.domain.model

data class Contact(
    val id: String = "",
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val profileImageUrl: String? = null,
    val isSaved: Boolean = false,
    val isEditable: Boolean = true
)
