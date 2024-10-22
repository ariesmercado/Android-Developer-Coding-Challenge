package com.ariesmercado.itunesmasterdetail.domain

import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesIdsUseCase @Inject constructor(private val repository: ItunesRepository) {
    operator fun invoke(): Flow<List<Long>> {
        return repository.getFavoriteTrackIds()
    }
}