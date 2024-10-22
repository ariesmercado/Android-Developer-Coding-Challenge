package com.ariesmercado.itunesmasterdetail.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.ariesmercado.itunesmasterdetail.common.BaseDao
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesFavorites
import kotlinx.coroutines.flow.Flow

@Dao
interface ItunesFavoriteDao : BaseDao<ItunesFavorites> {

    @Query("SELECT * FROM $TABLE_NAME")
    fun all(): Flow<List<ItunesFavorites>>

    @Query("SELECT trackId FROM $TABLE_NAME")
    fun allIds(): Flow<List<Long>>

    @Query("SELECT * FROM $TABLE_NAME WHERE trackId = :id")
    fun get(id: Int): ItunesFavorites?

    @Query("SELECT * FROM $TABLE_NAME WHERE searchTerm = :term")
    suspend fun getFavoritesByTerm(term: String): List<ItunesFavorites>?

    @Query("SELECT * FROM $TABLE_NAME ORDER BY trackId DESC LIMIT 1")
    fun getFirst(): Flow<ItunesFavorites?>

    @Query("DELETE FROM $TABLE_NAME")
    fun nukeTable()

    companion object {
        const val TABLE_NAME = "ItunesFavorites"
    }
}