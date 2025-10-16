package com.sumeyrapolat.nomi.presentation.contacts

import android.net.Uri
import com.sumeyrapolat.nomi.domain.model.Contact

data class ContactsUiState(
    val contacts: List<Contact> = emptyList(),
    val isLoading: Boolean = false,
    val isContactCreated: Boolean = false,
    val toastMessage: String? = null,
    val error: String? = null,
    val selectedImageUri: Uri? = null,

    // 🔍 search
    val searchQuery: String = "",
    val searchFocused: Boolean = false,
    val recentSearches: List<String> = emptyList()
) {
    // filtreleme (ad + soyad birlikte arama, boşluk destekli)
    val filtered: List<Contact>
        get() {
            val q = searchQuery.trim()
            if (q.isEmpty()) return contacts
            val parts = q.split(" ").filter { it.isNotBlank() }
            return contacts.filter { c ->
                val full = "${c.firstName} ${c.lastName}".trim()
                parts.all { p -> full.contains(p, ignoreCase = true) }
            }
        }

    val isEmptyResult: Boolean
        get() = searchQuery.isNotBlank() && filtered.isEmpty()
}
