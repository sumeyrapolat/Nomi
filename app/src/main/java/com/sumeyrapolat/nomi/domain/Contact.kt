package com.sumeyrapolat.nomi.domain

data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val avatarResId: Int? = null
)
