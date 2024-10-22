package com.ariesmercado.itunesmasterdetail.di

import com.ariesmercado.itunesmasterdetail.common.DataStorageHelper
import com.ariesmercado.itunesmasterdetail.common.NetworkHelper
import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepository
import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepositoryImpl
import com.ariesmercado.itunesmasterdetail.data.source.local.database.AppDatabase
import com.ariesmercado.itunesmasterdetail.data.source.remote.api.ItunesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideItunesRepository(
        api: ItunesApi,
        dataStorageHelper: DataStorageHelper,
        networkHelper: NetworkHelper,
        database: AppDatabase,
    ): ItunesRepository {
        return ItunesRepositoryImpl(api, dataStorageHelper, networkHelper, database)
    }
}