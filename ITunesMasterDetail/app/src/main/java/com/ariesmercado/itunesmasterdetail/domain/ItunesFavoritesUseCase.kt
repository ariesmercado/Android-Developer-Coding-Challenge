package com.ariesmercado.itunesmasterdetail.domain

import com.ariesmercado.itunesmasterdetail.common.Resource
import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepository
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.Movie
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ItunesFavoritesUseCase @Inject constructor(
    private val repository: ItunesRepository,
) {
    operator fun invoke(movie: Movie, isFavorite: Boolean, term: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val message = if (isFavorite) {
            repository.deleteFavorite(movie, term)
            "Unfavorited"
        } else {
            repository.addFavorite(movie, term)
            "Favorited"
        }
        emit(Resource.Success(message))
    }.catch { _ ->
        emit(Resource.Error("Unable to save to favorite"))
    }
}