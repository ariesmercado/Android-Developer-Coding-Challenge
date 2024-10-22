package com.ariesmercado.itunesmasterdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ariesmercado.itunesmasterdetail.common.Resource
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.ItunesResponse
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.Movie
import com.ariesmercado.itunesmasterdetail.domain.GetDisplayDateUseCase
import com.ariesmercado.itunesmasterdetail.domain.GetFavoritesIdsUseCase
import com.ariesmercado.itunesmasterdetail.domain.GetFavoritesUseCase
import com.ariesmercado.itunesmasterdetail.domain.ItunesFavoritesUseCase
import com.ariesmercado.itunesmasterdetail.domain.ItunesSearchUseCase
import com.ariesmercado.itunesmasterdetail.domain.SaveScreenHistoryUseCase
import com.ariesmercado.itunesmasterdetail.presenter.search.ItunesListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class ItunesListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var itunesSearchUseCase: ItunesSearchUseCase
    @Mock
    lateinit var itunesFavoritesUseCase: ItunesFavoritesUseCase
    @Mock
    lateinit var getFavoritesIdsUseCase: GetFavoritesIdsUseCase
    @Mock
    lateinit var getDisplayDateUseCase: GetDisplayDateUseCase
    @Mock
    lateinit var getFavoritesUseCase: GetFavoritesUseCase
    @Mock
    lateinit var saveScreenHistoryUseCase: SaveScreenHistoryUseCase

    private lateinit var viewModel: ItunesListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = ItunesListViewModel(
            itunesSearchUseCase,
            itunesFavoritesUseCase,
            getFavoritesIdsUseCase,
            getDisplayDateUseCase,
            getFavoritesUseCase,
            saveScreenHistoryUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search with valid term updates state with search results`() = runTest {
        val term = "test"
        val expectedResponse = Resource.Success(ItunesResponse())

        `when`(itunesSearchUseCase(term)).thenReturn(flowOf(expectedResponse))

        viewModel.search(term)

        viewModel.iTunesState.collect { state ->
            assertTrue(state is Resource.Success)
            assertEquals(expectedResponse.data, (state as Resource.Success).data)
        }
    }

    @Test
    fun `search with empty term sets state to loading`() = runTest {
        viewModel.search("")

        viewModel.iTunesState.collect { state ->
            assertTrue(state is Resource.Loading)
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
        val screen = "ITUNES_LIST"
        val movie = Movie()
        val searchTerm = "test"

        viewModel.saveScreenState(screen, movie, searchTerm)

        verify(saveScreenHistoryUseCase).invoke(screen, movie, searchTerm)
    }
}
