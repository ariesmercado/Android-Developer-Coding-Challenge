package com.ariesmercado.itunesmasterdetail.data.source.remote.api

import com.ariesmercado.itunesmasterdetail.data.source.remote.model.ItunesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val DEFAULT_COUNTRY = "au"
const val DEFAULT_MEDIA = "movie"

interface ItunesApi {

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("country") country: String = DEFAULT_COUNTRY,
        @Query("media") media: String = DEFAULT_MEDIA
    ): ItunesResponse
}
