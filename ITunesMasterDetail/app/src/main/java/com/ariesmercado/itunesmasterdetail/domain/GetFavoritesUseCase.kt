package com.ariesmercado.itunesmasterdetail.domain

import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepository
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesFavorites
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(private val repository: ItunesRepository) {
    operator fun invoke(): Flow<List<ItunesFavorites>> {
        return repository.getFavorites()
    }
}
