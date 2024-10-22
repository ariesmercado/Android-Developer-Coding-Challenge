package com.ariesmercado.itunesmasterdetail.domain

import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepository
import javax.inject.Inject

class LastScreenUseCase @Inject constructor(private val repository: ItunesRepository) {
    suspend operator fun invoke(): String? {
        return repository.getLastScreen()
    }
}