package com.sumeyrapolat.nomi.di

import android.content.Context
import android.content.SharedPreferences
import com.sumeyrapolat.nomi.data.RecentSearchManager
import com.sumeyrapolat.nomi.data.repositoryImpl.RecentSearchesRepositoryImpl
import com.sumeyrapolat.nomi.domain.repository.RecentSearchesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideRecentSearchesRepository(
        prefs: SharedPreferences
    ): RecentSearchesRepository = RecentSearchesRepositoryImpl(prefs)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("nomi_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideRecentSearchManager(prefs: SharedPreferences): RecentSearchManager =
        RecentSearchManager(prefs)

}
