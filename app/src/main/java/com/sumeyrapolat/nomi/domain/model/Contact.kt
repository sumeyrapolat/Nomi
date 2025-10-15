package com.sumeyrapolat.nomi.domain.model

data class Contact(
    val id: String = "",              // Swagger’da id "string" dönüyor
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val profileImageUrl: String? = null,
    val isSaved: Boolean = false,     // Rehberde kayıtlı mı?
    val isEditable: Boolean = true    // Düzenlenebilir mi?
)
