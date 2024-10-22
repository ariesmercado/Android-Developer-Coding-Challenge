package com.ariesmercado.itunesmasterdetail.di

import android.content.Context
import androidx.room.Room
import com.ariesmercado.itunesmasterdetail.common.constant.Constants.DB
import com.ariesmercado.itunesmasterdetail.data.source.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DB
        ).build()
    }

    @Provides
    fun provideFavoriteDao(database: AppDatabase) = database.iTunesFavoriteDao()

    @Provides
    fun provideLastVisitDao(database: AppDatabase) = database.iTunesLastVisitDao()
}