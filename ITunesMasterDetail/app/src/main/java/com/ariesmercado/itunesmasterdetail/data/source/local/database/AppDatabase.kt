package com.ariesmercado.itunesmasterdetail.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ariesmercado.itunesmasterdetail.data.source.local.dao.ItunesFavoriteDao
import com.ariesmercado.itunesmasterdetail.data.source.local.dao.ItunesLastMovieVisitDao
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesFavorites
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesLastMovie

const val VERSION_NUMBER = 1

@Database(
    entities = [ItunesFavorites::class, ItunesLastMovie::class],
    version = VERSION_NUMBER,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun iTunesFavoriteDao(): ItunesFavoriteDao
    abstract fun iTunesLastVisitDao(): ItunesLastMovieVisitDao
}