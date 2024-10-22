package com.ariesmercado.itunesmasterdetail.domain

import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepository
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesFavorites
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesLastMovie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LastMovieDetailsUseCase @Inject constructor(private val repository: ItunesRepository) {
    operator fun invoke(): Flow<ItunesLastMovie?> {
        return repository.getLastMovieDetails()
    }
}
