package com.sumeyrapolat.nomi.di

import com.sumeyrapolat.nomi.domain.repository.ContactRepository
import com.sumeyrapolat.nomi.domain.usecase.AddContactUseCase
import com.sumeyrapolat.nomi.domain.usecase.GetContactsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideAddContactUseCase(repository: ContactRepository) =
        AddContactUseCase(repository)

    @Provides
    @Singleton
    fun provideGetContactsUseCase(repository: ContactRepository) =
        GetContactsUseCase(repository)
}
