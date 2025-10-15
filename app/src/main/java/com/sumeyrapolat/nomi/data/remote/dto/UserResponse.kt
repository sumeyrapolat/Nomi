package com.sumeyrapolat.nomi.data.remote.dto

data class UserResponse(
    val id: String,
    val createdAt: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val profileImageUrl: String?
)
