package com.sumeyrapolat.nomi.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("recent_searches")

@Singleton
class RecentSearchManager @Inject constructor(
    private val context: Context
) {
    private val KEY = stringSetPreferencesKey("recent_searches_list")

    val recentSearches: Flow<List<String>> =
        context.dataStore.data.map { it[KEY]?.toList()?.sortedBy { s -> s.lowercase() } ?: emptyList() }

    suspend fun add(query: String, max: Int = 10) {
        val clean = query.trim()
        if (clean.isEmpty()) return
        context.dataStore.edit { prefs ->
            val set = (prefs[KEY] ?: emptySet()).toMutableList()
            set.remove(clean)
            set.add(0, clean)            // en yeni başa
            if (set.size > max) set.removeLast()
            prefs[KEY] = set.toSet()
        }
    }

    suspend fun remove(query: String) {
        context.dataStore.edit { prefs ->
            val set = (prefs[KEY] ?: emptySet()).toMutableSet()
            set.remove(query)
            prefs[KEY] = set
        }
    }

    suspend fun clear() {
        context.dataStore.edit { prefs -> prefs.remove(KEY) }
    }
}
