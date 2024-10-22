package com.ariesmercado.itunesmasterdetail.data.source.remote.model

import android.os.Parcelable
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesFavorites
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesLastMovie
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItunesResponse(
    @SerializedName("resultCount") val resultCount: Int? = null,
    @SerializedName("results") val results: List<Movie>? = emptyList()
): Parcelable

@Parcelize
data class Movie(
    @SerializedName("wrapperType") val wrapperType: String? = null,
    @SerializedName("kind") val kind: String? = null,
    @SerializedName("trackId") val trackId: Long? = null,
    @SerializedName("artistName") val artistName: String? = null,
    @SerializedName("trackName") val trackName: String? = null,
    @SerializedName("trackCensoredName") val trackCensoredName: String? = null,
    @SerializedName("trackViewUrl") val trackViewUrl: String? = null,
    @SerializedName("previewUrl") val previewUrl: String? = null,
    @SerializedName("artworkUrl30") val artworkUrl30: String? = null,
    @SerializedName("artworkUrl60") val artworkUrl60: String? = null,
    @SerializedName("artworkUrl100") val artworkUrl100: String? = null,
    @SerializedName("collectionPrice") val collectionPrice: Double? = null,
    @SerializedName("trackPrice") val trackPrice: Double? = null,
    @SerializedName("trackRentalPrice") val trackRentalPrice: Double? = null,
    @SerializedName("collectionHdPrice") val collectionHdPrice: Double? = null,
    @SerializedName("trackHdPrice") val trackHdPrice: Double? = null,
    @SerializedName("trackHdRentalPrice") val trackHdRentalPrice: Double? = null,
    @SerializedName("releaseDate") val releaseDate: String? = null,
    @SerializedName("collectionExplicitness") val collectionExplicitness: String? = null,
    @SerializedName("trackExplicitness") val trackExplicitness: String? = null,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Long? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("currency") val currency: String? = null,
    @SerializedName("primaryGenreName") val primaryGenreName: String? = null,
    @SerializedName("contentAdvisoryRating") val contentAdvisoryRating: String? = null,
    @SerializedName("shortDescription") val shortDescription: String? = null,
    @SerializedName("longDescription") val longDescription: String? = null
): Parcelable

fun Movie.toFavorite(term: String): ItunesFavorites {
    return ItunesFavorites(
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
        longDescription = this.longDescription,
        searchTerm = term
    )
}

