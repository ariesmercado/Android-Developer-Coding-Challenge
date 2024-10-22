package com.ariesmercado.itunesmasterdetail.domain

import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepository
import javax.inject.Inject

class LoadDateUseCase @Inject constructor(private val repository: ItunesRepository) {
    suspend operator fun invoke() {
        return repository.loadDate()
    }
}