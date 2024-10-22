package com.ariesmercado.itunesmasterdetail.presenter.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesLastMovie
import com.ariesmercado.itunesmasterdetail.domain.LastMovieDetailsUseCase
import com.ariesmercado.itunesmasterdetail.domain.LastScreenUseCase
import com.ariesmercado.itunesmasterdetail.domain.LoadDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val loadDateUseCase: LoadDateUseCase,
    private val lastScreenUseCase: LastScreenUseCase,
    private val lastMovieDetailsUseCase: LastMovieDetailsUseCase,
): ViewModel() {

    val lastMovie: StateFlow<ItunesLastMovie?> = lastMovieDetailsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun loadDate() {
        viewModelScope.launch {
            loadDateUseCase.invoke()
        }
    }

    suspend fun getLastScreen() = lastScreenUseCase()
}