package com.sumeyrapolat.nomi.data

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecentSearchManager(
    private val sharedPrefs: SharedPreferences
) {

    companion object {
        private const val KEY_SEARCHES = "recent_searches"
    }

    private val _recentSearches = MutableStateFlow(loadSearches())
    val recentSearches = _recentSearches.asStateFlow()

    /** Yeni bir arama ekler (tekrar varsa başa alır) */
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    suspend fun add(query: String) {
        if (query.isBlank()) return
        val updated = _recentSearches.value.toMutableList().apply {
            remove(query)
            add(0, query)
            if (size > 10) removeLast()
        }
        println("✅ Yeni arama eklendi: $query | Güncel liste: $updated")
        save(updated)
    }


    /** Belirli bir aramayı siler */
    suspend fun remove(query: String) {
        val updated = _recentSearches.value.toMutableList().apply { remove(query) }
        save(updated)
    }

    /** Tüm geçmişi temizler */
    suspend fun clear() {
        save(emptyList())
    }

    private suspend fun save(list: List<String>) {
        withContext(Dispatchers.IO) {
            sharedPrefs.edit()
                .putStringSet(KEY_SEARCHES, list.toSet())
                .apply()
            _recentSearches.update { list }
        }
    }

    private fun loadSearches(): List<String> {
        return sharedPrefs.getStringSet(KEY_SEARCHES, emptySet())?.toList() ?: emptyList()
    }
}
