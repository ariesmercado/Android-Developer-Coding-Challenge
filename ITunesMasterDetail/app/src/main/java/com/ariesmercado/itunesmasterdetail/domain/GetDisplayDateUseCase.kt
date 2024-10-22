package com.ariesmercado.itunesmasterdetail.domain

import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepository
import javax.inject.Inject

class GetDisplayDateUseCase @Inject constructor(private val repository: ItunesRepository) {
    operator fun invoke(): String {
        return repository.displayDate
    }
}