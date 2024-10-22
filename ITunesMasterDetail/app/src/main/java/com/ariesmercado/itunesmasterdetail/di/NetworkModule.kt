package com.ariesmercado.itunesmasterdetail.di

import com.ariesmercado.itunesmasterdetail.BuildConfig
import com.ariesmercado.itunesmasterdetail.common.constant.Constants
import com.ariesmercado.itunesmasterdetail.common.provideGsonApi
import com.ariesmercado.itunesmasterdetail.data.source.remote.api.ItunesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideHttpClient(okHttpBuilder: OkHttpClient.Builder) = okHttpBuilder.build()

    @Provides
    fun provideHttpClientBuilder(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
        }
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return httpLoggingInterceptor
    }

    @Provides
    fun provideItunesApi(client: OkHttpClient): ItunesApi {
        return provideGsonApi(Constants.BASE_URL, client)
    }
}