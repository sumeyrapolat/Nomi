package com.sumeyrapolat.nomi.di

import com.sumeyrapolat.nomi.data.remote.ContactApi
import com.sumeyrapolat.nomi.data.repository.ContactRepositoryImpl
import com.sumeyrapolat.nomi.domain.repository.ContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://146.59.52.68:11235/" // ✅ düzeltildi

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()) // ✅ eklendi
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideContactApi(retrofit: Retrofit): ContactApi =
        retrofit.create(ContactApi::class.java)

    @Provides
    @Singleton
    fun provideContactRepository(api: ContactApi): ContactRepository =
        ContactRepositoryImpl(api)
}
