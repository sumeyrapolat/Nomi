package com.sumeyrapolat.nomi.data.remote

import com.sumeyrapolat.nomi.data.remote.dto.UserResponse

// ApiResponse.kt
data class ApiResponse<T>(
    val success: Boolean,
    val messages: List<String>?,
    val data: T?,
    val status: Int
)

// UsersWrapper.kt
data class UsersWrapper(
    val users: List<UserResponse>
)
