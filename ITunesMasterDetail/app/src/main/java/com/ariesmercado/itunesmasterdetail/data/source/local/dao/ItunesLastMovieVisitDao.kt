package com.ariesmercado.itunesmasterdetail.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.ariesmercado.itunesmasterdetail.common.BaseDao
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesFavorites
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesLastMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface ItunesLastMovieVisitDao : BaseDao<ItunesLastMovie> {

    @Query("SELECT * FROM $TABLE_NAME")
    fun all(): Flow<List<ItunesLastMovie>>

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun nukeTable()

    @Query("SELECT * FROM ${ItunesFavoriteDao.TABLE_NAME} ORDER BY trackId DESC LIMIT 1")
    fun getLastMovieVisited(): Flow<ItunesLastMovie?>

    companion object {
        const val TABLE_NAME = "ItunesLastMovie"
    }
}