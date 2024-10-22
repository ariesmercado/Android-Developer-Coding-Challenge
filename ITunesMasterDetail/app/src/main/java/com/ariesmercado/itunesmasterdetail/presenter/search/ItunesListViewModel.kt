package com.ariesmercado.itunesmasterdetail.presenter.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariesmercado.itunesmasterdetail.common.Resource
import com.ariesmercado.itunesmasterdetail.common.constant.Constants.ITUNES_LIST
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesFavorites
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.ItunesResponse
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.Movie
import com.ariesmercado.itunesmasterdetail.domain.GetDisplayDateUseCase
import com.ariesmercado.itunesmasterdetail.domain.GetFavoritesIdsUseCase
import com.ariesmercado.itunesmasterdetail.domain.GetFavoritesUseCase
import com.ariesmercado.itunesmasterdetail.domain.ItunesFavoritesUseCase
import com.ariesmercado.itunesmasterdetail.domain.ItunesSearchUseCase
import com.ariesmercado.itunesmasterdetail.domain.SaveScreenHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ItunesListViewModel@Inject constructor(
    private val itunesSearchUseCase: ItunesSearchUseCase,
    private val itunesFavoritesUseCase: ItunesFavoritesUseCase,
    private val getFavoritesIdsUseCase: GetFavoritesIdsUseCase,
    private val getDisplayDateUseCase: GetDisplayDateUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val saveScreenHistoryUseCase: SaveScreenHistoryUseCase
) : ViewModel() {

    private var job: Job? = null

    private val _iTunesState =
        MutableStateFlow<Resource<ItunesResponse>>(Resource.Loading())
    val iTunesState = _iTunesState.asStateFlow()

    val favoriteIds: StateFlow<List<Long>> = getFavoritesIdsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val favoriteMovies: StateFlow<List<ItunesFavorites>> = getFavoritesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val displayDate: String = getDisplayDateUseCase()

    fun search(term: String) {
        job?.cancel()
        job = viewModelScope.launch {
            if (term.isNotBlank()) {
                itunesSearchUseCase(term).collect { result ->
                    saveScreenState(screen = ITUNES_LIST, searchTerm = term)
                    _iTunesState.value = result
                }
            } else {
                _iTunesState.value = Resource.Loading()
            }
        }
    }

    fun toggleFavorite(movie: Movie, isFavorite: Boolean, term: String) {
        itunesFavoritesUseCase(movie, isFavorite, term).onEach { result ->
        }.launchIn(viewModelScope)
    }

    fun saveScreenState(
        screen: String,
        movie: Movie? = null,
        searchTerm: String? = null,
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