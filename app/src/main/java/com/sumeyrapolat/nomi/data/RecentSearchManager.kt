package com.sumeyrapolat.nomi.data

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

class RecentSearchManager(
    private val sharedPrefs: SharedPreferences
) {

    companion object {
        private const val KEY_SEARCHES = "recent_searches"
    }

    private val _recentSearches = MutableStateFlow(loadSearches())
    val recentSearches = _recentSearches.asStateFlow()

    /** Yeni bir arama ekler (tekrar varsa başa alır) */
    suspend fun add(query: String) {
        val normalized = query.trim()
        if (normalized.isEmpty()) return
        val updated = _recentSearches.value.toMutableList().apply {
            remove(normalized)
            add(0, normalized)
            if (size > 10) removeLast()
        }
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
                .putString(KEY_SEARCHES, list.toJson())
                .apply()
            _recentSearches.update { list }
        }
    }

    private fun loadSearches(): List<String> {
        val raw = sharedPrefs.getString(KEY_SEARCHES, null) ?: return emptyList()
        return runCatching {
            val jsonArray = JSONArray(raw)
            List(jsonArray.length()) { index -> jsonArray.optString(index) }
        }.getOrDefault(emptyList())
    }

    private fun List<String>.toJson(): String = JSONArray().apply {
        forEach { put(it) }
    }.toString()
}
