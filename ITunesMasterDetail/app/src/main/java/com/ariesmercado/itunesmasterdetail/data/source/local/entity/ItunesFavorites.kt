package com.ariesmercado.itunesmasterdetail.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.ItunesResponse
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.Movie

@Entity
data class ItunesFavorites(
    @PrimaryKey val trackId: Long? = null,
    val artistName: String? = null,
    val trackName: String? = null,
    val trackViewUrl: String? = null,
    val previewUrl: String? = null,
    val artworkUrl100: String? = null,
    val trackPrice: Double? = null,
    val releaseDate: String? = null,
    val currency: String? = null,
    val primaryGenreName: String? = null,
    val longDescription: String? = null,
    val searchTerm: String? = null
)

fun List<ItunesFavorites>.toMovies(): ItunesResponse {
    val movies = this.map {
        Movie(
            trackId = it.trackId,
            artistName = it.artistName,
            trackName = it.trackName,
            trackViewUrl = it.trackViewUrl,
            previewUrl = it.previewUrl,
            artworkUrl100 = it.artworkUrl100,
            trackPrice = it.trackPrice,
            releaseDate = it.releaseDate,
            currency = it.currency,
            primaryGenreName = it.primaryGenreName,
            longDescription = it.longDescription
        )
    }

    return ItunesResponse(
        resultCount = movies.size,
        results = movies
    )
}

fun ItunesFavorites.toMovie(): Movie {
    return Movie(
        trackId = this.trackId,
        artistName = this.artistName,
        trackName = this.trackName,
        trackViewUrl = this.trackViewUrl,
        previewUrl = this.previewUrl,
        artworkUrl100 = this.artworkUrl100,
        trackPrice = this.trackPrice,
        releaseDate = this.releaseDate,
        currency = this.currency,
        primaryGenreName = this.primaryGenreName,
        longDescription = this.longDescription
    )
}
