package com.sumeyrapolat.nomi.domain.repository

import kotlinx.coroutines.flow.Flow

interface RecentSearchesRepository {
    fun getSearches(): Flow<List<String>>
    suspend fun saveSearches(searches: List<String>)
}
