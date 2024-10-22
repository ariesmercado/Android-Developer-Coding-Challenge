package com.ariesmercado.itunesmasterdetail.domain

import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepository
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.Movie
import javax.inject.Inject

class SaveScreenHistoryUseCase @Inject constructor(private val repository: ItunesRepository) {
    suspend operator fun invoke(
        screen: String,
        movie: Movie? = null,
        searchTerm: String? = null
    ) {
        repository.saveScreenHistory(
            screen = screen,
            movie = movie,
            searchTerm = searchTerm,
        )
    }
}