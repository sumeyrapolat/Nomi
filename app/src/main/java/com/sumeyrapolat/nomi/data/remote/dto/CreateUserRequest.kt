package com.sumeyrapolat.nomi.data.remote.dto

data class CreateUserRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val profileImageUrl: String? = null
)
