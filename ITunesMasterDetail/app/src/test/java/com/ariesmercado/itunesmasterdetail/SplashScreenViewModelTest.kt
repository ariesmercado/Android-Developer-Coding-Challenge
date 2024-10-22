package com.ariesmercado.itunesmasterdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesLastMovie
import com.ariesmercado.itunesmasterdetail.domain.LastMovieDetailsUseCase
import com.ariesmercado.itunesmasterdetail.domain.LastScreenUseCase
import com.ariesmercado.itunesmasterdetail.domain.LoadDateUseCase
import com.ariesmercado.itunesmasterdetail.presenter.splash.SplashScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class SplashScreenViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var loadDateUseCase: LoadDateUseCase

    @Mock
    lateinit var lastScreenUseCase: LastScreenUseCase

    @Mock
    lateinit var lastMovieDetailsUseCase: LastMovieDetailsUseCase

    @InjectMocks
    lateinit var viewModel: SplashScreenViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `lastMovie returns correct last movie details`() = runTest {
        val expectedMovie = ItunesLastMovie(trackId = 1, trackName = "Test Movie", artworkUrl100 = "url")
        `when`(lastMovieDetailsUseCase()).thenReturn(flowOf(expectedMovie))

        val lastMovie = viewModel.lastMovie

        lastMovie.collect { movie ->
            assertEquals(expectedMovie, movie)
        }
    }

    @Test
    fun `loadDate invokes loadDateUseCase`() = runTest {
        viewModel.loadDate()

        verify(loadDateUseCase).invoke()
    }

    @Test
    fun `getLastScreen returns correct last screen`() = runBlocking {
        val expectedScreen = "MOVIE_DETAILS"
        `when`(lastScreenUseCase()).thenReturn(expectedScreen)

        val lastScreen = viewModel.getLastScreen()

        assertEquals(expectedScreen, lastScreen)
    }
}
