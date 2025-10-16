package com.sumeyrapolat.nomi.di

import android.content.Context
import android.content.SharedPreferences
import com.sumeyrapolat.nomi.data.RecentSearchManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("nomi_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideRecentSearchManager(prefs: SharedPreferences): RecentSearchManager =
        RecentSearchManager(prefs)

}
