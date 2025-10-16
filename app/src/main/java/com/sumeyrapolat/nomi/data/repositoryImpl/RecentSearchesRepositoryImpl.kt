package com.sumeyrapolat.nomi.data.repositoryImpl

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.sumeyrapolat.nomi.domain.repository.RecentSearchesRepository

class RecentSearchesRepositoryImpl(
    private val sharedPrefs: SharedPreferences
) : RecentSearchesRepository {

    companion object {
        private const val KEY_SEARCHES = "recent_searches"
    }

    private val _searches = MutableStateFlow(loadSearches())
    override fun getSearches(): Flow<List<String>> = _searches

    override suspend fun saveSearches(searches: List<String>) {
        withContext(Dispatchers.IO) {
            sharedPrefs.edit()
                .putStringSet(KEY_SEARCHES, searches.toSet())
                .apply()
            _searches.update { searches }
        }
    }

    private fun loadSearches(): List<String> {
        return sharedPrefs.getStringSet(KEY_SEARCHES, emptySet())?.toList() ?: emptyList()
    }
}
