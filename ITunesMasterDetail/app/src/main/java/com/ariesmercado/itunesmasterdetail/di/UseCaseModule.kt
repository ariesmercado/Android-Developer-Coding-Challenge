package com.ariesmercado.itunesmasterdetail.di

import com.ariesmercado.itunesmasterdetail.common.DataStorageHelper
import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepository
import com.ariesmercado.itunesmasterdetail.domain.GetDisplayDateUseCase
import com.ariesmercado.itunesmasterdetail.domain.GetFavoritesIdsUseCase
import com.ariesmercado.itunesmasterdetail.domain.GetFavoritesUseCase
import com.ariesmercado.itunesmasterdetail.domain.ItunesFavoritesUseCase
import com.ariesmercado.itunesmasterdetail.domain.ItunesSearchUseCase
import com.ariesmercado.itunesmasterdetail.domain.LastMovieDetailsUseCase
import com.ariesmercado.itunesmasterdetail.domain.LastScreenUseCase
import com.ariesmercado.itunesmasterdetail.domain.LoadDateUseCase
import com.ariesmercado.itunesmasterdetail.domain.SaveScreenHistoryUseCase
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
    fun provideItunesSearchUseCase(
        itunesRepository: ItunesRepository,
        dataStorageHelper: DataStorageHelper
    ): ItunesSearchUseCase {
        return ItunesSearchUseCase(itunesRepository, dataStorageHelper)
    }

    @Provides
    @Singleton
    fun provideItunesFavoritesUseCase(
        itunesRepository: ItunesRepository,
    ): ItunesFavoritesUseCase {
        return ItunesFavoritesUseCase(itunesRepository)
    }

    @Provides
    @Singleton
    fun provideGetFavoritesIdsUseCase(
        itunesRepository: ItunesRepository,
    ): GetFavoritesIdsUseCase {
        return GetFavoritesIdsUseCase(itunesRepository)
    }

    @Provides
    @Singleton
    fun provideGetFavoritesUseCase(
        itunesRepository: ItunesRepository,
    ): GetFavoritesUseCase {
        return GetFavoritesUseCase(itunesRepository)
    }

    @Provides
    @Singleton
    fun provideGetDisplayDateUseCase(
        itunesRepository: ItunesRepository,
    ): GetDisplayDateUseCase {
        return GetDisplayDateUseCase(itunesRepository)
    }

    @Provides
    @Singleton
    fun provideLoadDisplayDateUseCase(
        itunesRepository: ItunesRepository,
    ): LoadDateUseCase {
        return LoadDateUseCase(itunesRepository)
    }

    @Provides
    @Singleton
    fun provideSaveScreenHistoryUseCase(
        itunesRepository: ItunesRepository,
    ): SaveScreenHistoryUseCase {
        return SaveScreenHistoryUseCase(itunesRepository)
    }

    @Provides
    @Singleton
    fun provideLastMovieDetailsUseCase(
        itunesRepository: ItunesRepository,
    ): LastMovieDetailsUseCase {
        return LastMovieDetailsUseCase(itunesRepository)
    }

    @Provides
    @Singleton
    fun provideLastScreenUseCase(
        itunesRepository: ItunesRepository,
    ): LastScreenUseCase {
        return LastScreenUseCase(itunesRepository)
    }

}