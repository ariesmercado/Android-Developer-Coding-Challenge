package com.ariesmercado.itunesmasterdetail.presenter.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.Movie
import com.ariesmercado.itunesmasterdetail.domain.GetFavoritesIdsUseCase
import com.ariesmercado.itunesmasterdetail.domain.ItunesFavoritesUseCase
import com.ariesmercado.itunesmasterdetail.domain.SaveScreenHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItunesMovieDetailsViewModel@Inject constructor(
    private val itunesFavoritesUseCase: ItunesFavoritesUseCase,
    private val getFavoritesIdsUseCase: GetFavoritesIdsUseCase,
    private val saveScreenHistoryUseCase: SaveScreenHistoryUseCase
) : ViewModel() {
    val favoriteMovies: StateFlow<List<Long>> = getFavoritesIdsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    fun toggleFavorite(movie: Movie, isFavorite: Boolean, term: String) {
        itunesFavoritesUseCase(movie, isFavorite, term).onEach { _ ->
        }.launchIn(viewModelScope)
    }

    fun saveScreenState(
        screen: String,
        movie: Movie? = null,
        searchTerm: String? = null
    ) {
        viewModelScope.launch {
            saveScreenHistoryUseCase(
                screen,
                movie,
                searchTerm
            )
        }
    }
}