package com.ariesmercado.itunesmasterdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.Movie
import com.ariesmercado.itunesmasterdetail.domain.GetFavoritesIdsUseCase
import com.ariesmercado.itunesmasterdetail.domain.ItunesFavoritesUseCase
import com.ariesmercado.itunesmasterdetail.domain.SaveScreenHistoryUseCase
import com.ariesmercado.itunesmasterdetail.presenter.details.ItunesMovieDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class ItunesMovieDetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var itunesFavoritesUseCase: ItunesFavoritesUseCase
    @Mock
    lateinit var getFavoritesIdsUseCase: GetFavoritesIdsUseCase
    @Mock
    lateinit var saveScreenHistoryUseCase: SaveScreenHistoryUseCase

    private lateinit var viewModel: ItunesMovieDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = ItunesMovieDetailsViewModel(
            itunesFavoritesUseCase,
            getFavoritesIdsUseCase,
            saveScreenHistoryUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `favoriteMovies returns correct favorite ids`() = runTest {
        val expectedIds = listOf(1L, 2L, 3L)
        `when`(getFavoritesIdsUseCase()).thenReturn(flowOf(expectedIds))

        val favoriteMovies = viewModel.favoriteMovies

        favoriteMovies.collect { ids ->
            assertEquals(expectedIds, ids)
        }
    }

    @Test
    fun `toggleFavorite calls favorites use case`() = runTest {
        val movie = Movie()
        val isFavorite = true
        val term = "test"

        viewModel.toggleFavorite(movie, isFavorite, term)

        verify(itunesFavoritesUseCase).invoke(movie, isFavorite, term)
    }

    @Test
    fun `saveScreenState saves screen history`() = runTest {
        val screen = "MOVIE_DETAILS"
        val movie = Movie()
        val searchTerm = "test"

        viewModel.saveScreenState(screen, movie, searchTerm)

        verify(saveScreenHistoryUseCase).invoke(screen, movie, searchTerm)
    }
}
