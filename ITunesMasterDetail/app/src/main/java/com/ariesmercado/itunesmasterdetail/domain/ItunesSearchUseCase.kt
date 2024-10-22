package com.ariesmercado.itunesmasterdetail.domain

import com.ariesmercado.itunesmasterdetail.common.DataStorageHelper
import com.ariesmercado.itunesmasterdetail.common.Resource
import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepository
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.ItunesResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ItunesSearchUseCase @Inject constructor(
    private val repository: ItunesRepository,
    private val dataStorageHelper: DataStorageHelper
) {
    operator fun invoke(term: String): Flow<Resource<ItunesResponse>> = flow {
        emit(Resource.Loading())
        val data = repository.search(term = term)
        emit(Resource.Success(data))
    }.catch { _ ->
        dataStorageHelper.getValue<ItunesResponse>(term)?.let {
            emit(Resource.Success(it))
        } ?: run {
            emit(Resource.Error("An error encountered while searching"))
        }
    }
}