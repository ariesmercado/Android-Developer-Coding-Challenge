package com.ariesmercado.itunesmasterdetail.data.repository

import com.ariesmercado.itunesmasterdetail.common.DataStorageHelper
import com.ariesmercado.itunesmasterdetail.common.DateUtil
import com.ariesmercado.itunesmasterdetail.common.NetworkHelper
import com.ariesmercado.itunesmasterdetail.common.constant.PreferenceKey
import com.ariesmercado.itunesmasterdetail.data.source.local.database.AppDatabase
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesFavorites
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesLastMovie
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.toLastMovie
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.toMovies
import com.ariesmercado.itunesmasterdetail.data.source.remote.api.ItunesApi
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.ItunesResponse
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.Movie
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.toFavorite
import kotlinx.coroutines.flow.Flow

interface ItunesRepository {
    var displayDate: String

    suspend fun search(term: String): ItunesResponse

    suspend fun addFavorite(movie: Movie, term: String): Long

    suspend fun deleteFavorite(movie: Movie, term: String)

    suspend fun loadDate()

    suspend fun getLastScreen(): String?

    suspend fun saveScreenHistory(
        screen: String,
        movie: Movie? = null,
        searchTerm: String? = null
    )

    fun getFavoriteTrackIds(): Flow<List<Long>>

    fun getFavorites(): Flow<List<ItunesFavorites>>

    fun getLastMovieDetails(): Flow<ItunesLastMovie?>
}

class ItunesRepositoryImpl(
    private val api: ItunesApi,
    private val dataStorageHelper: DataStorageHelper,
    private val networkHelper: NetworkHelper,
    private val database: AppDatabase,
) : ItunesRepository {

    override var displayDate = ""

    override suspend fun search(term: String): ItunesResponse {
        return try {
            if (networkHelper.isInternetAvailable()) {
                api.search(term)
            } else {
                database.iTunesFavoriteDao().getFavoritesByTerm(term)?.toMovies()
                    ?: ItunesResponse(results = emptyList())
            }
        } catch (e: Exception) {
            database.iTunesFavoriteDao().getFavoritesByTerm(term)?.toMovies()
                ?: throw e
        }
    }

    override suspend fun addFavorite(movie: Movie, term: String): Long {
        return database.iTunesFavoriteDao().insert(movie.toFavorite(term))
    }

    override suspend fun deleteFavorite(movie: Movie, term: String) {
        return database.iTunesFavoriteDao().delete(movie.toFavorite(term))
    }

    override suspend fun loadDate() {
        val currentDate = DateUtil.getCurrentDate()
        displayDate = dataStorageHelper.getValue<String>(PreferenceKey.LAST_TIME_ENTRY) ?: currentDate
        dataStorageHelper.saveValue(PreferenceKey.LAST_TIME_ENTRY, currentDate)
    }

    override suspend fun getLastScreen(): String? = dataStorageHelper.getValue(PreferenceKey.SCREEN)

    override suspend fun saveScreenHistory(
        screen: String,
        movie: Movie?,
        searchTerm: String?,
    ) {
        dataStorageHelper.apply {
            saveValue(PreferenceKey.SCREEN, screen)
            searchTerm?.let { saveValue(PreferenceKey.SEARCH_TERM, it) }
        }

        movie?.let {
            val movieVisited = movie.toLastMovie()
            database.iTunesLastVisitDao().nukeTable()

            database.iTunesLastVisitDao().insert(
                movieVisited
            )
        }
    }

    override fun getFavoriteTrackIds(): Flow<List<Long>> = database.iTunesFavoriteDao().allIds()

    override fun getFavorites(): Flow<List<ItunesFavorites>> = database.iTunesFavoriteDao().all()

    override fun getLastMovieDetails(): Flow<ItunesLastMovie?> =
        database.iTunesLastVisitDao().getLastMovieVisited()
}