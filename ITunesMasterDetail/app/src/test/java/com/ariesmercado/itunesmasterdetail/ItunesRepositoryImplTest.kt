package com.ariesmercado.itunesmasterdetail

import com.ariesmercado.itunesmasterdetail.common.DataStorageHelper
import com.ariesmercado.itunesmasterdetail.common.NetworkHelper
import com.ariesmercado.itunesmasterdetail.common.constant.PreferenceKey
import com.ariesmercado.itunesmasterdetail.data.repository.ItunesRepositoryImpl
import com.ariesmercado.itunesmasterdetail.data.source.local.database.AppDatabase
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesFavorites
import com.ariesmercado.itunesmasterdetail.data.source.remote.api.ItunesApi
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.ItunesResponse
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.Movie
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*

class ItunesRepositoryImplTest {

    private lateinit var api: ItunesApi
    private lateinit var dataStorageHelper: DataStorageHelper
    private lateinit var networkHelper: NetworkHelper
    private lateinit var database: AppDatabase
    private lateinit var repository: ItunesRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        api = mock(ItunesApi::class.java)
        dataStorageHelper = mock(DataStorageHelper::class.java)
        networkHelper = mock(NetworkHelper::class.java)
        database = mock(AppDatabase::class.java)

        repository = ItunesRepositoryImpl(api, dataStorageHelper, networkHelper, database)
    }

    @Test
    fun `search returns API results when internet is available`() = runTest(testDispatcher) {
        val expectedResponse = ItunesResponse(results = listOf(Movie(trackId = 1)))
        `when`(networkHelper.isInternetAvailable()).thenReturn(true)
        `when`(api.search(anyString())).thenReturn(expectedResponse)

        val result = repository.search("testTerm")

        assertEquals(expectedResponse, result)
        verify(api).search("testTerm")
    }

    @Test
    fun `search returns local favorites when internet is not available`() = runTest(testDispatcher) {
        val movie = Movie(trackId = 1)
        val favorites = ItunesFavorites(trackId = 1)
        val expectedResponse = ItunesResponse(results = listOf(movie))
        `when`(networkHelper.isInternetAvailable()).thenReturn(false)
        `when`(database.iTunesFavoriteDao().getFavoritesByTerm(anyString())).thenReturn(listOf(favorites))

        val result = repository.search("testTerm")

        assertEquals(expectedResponse, result)
    }

    @Test(expected = Exception::class)
    fun `search throws exception when internet is not available and no local data`() = runTest(testDispatcher) {
        `when`(networkHelper.isInternetAvailable()).thenReturn(false)
        `when`(database.iTunesFavoriteDao().getFavoritesByTerm(anyString())).thenReturn(null)

        repository.search("testTerm")
    }

    @Test
    fun `addFavorite inserts a favorite movie into the database`() = runTest(testDispatcher) {
        val movie = Movie(trackId = 1)
        `when`(database.iTunesFavoriteDao().insert(any())).thenReturn(1L)

        val result = repository.addFavorite(movie, "testTerm")

        assertEquals(1L, result)
        verify(database.iTunesFavoriteDao()).insert(any())
    }

    @Test
    fun `deleteFavorite removes a favorite movie from the database`() = runBlocking {
        val movie = Movie(trackId = 1)

        repository.deleteFavorite(movie, "testTerm")

        verify(database.iTunesFavoriteDao()).delete(any())
    }

    @Test
    fun `loadDate updates displayDate with last saved date`() = runTest(testDispatcher) {
        val expectedDate = "2023-10-01"
        `when`(dataStorageHelper.getValue<String>(PreferenceKey.LAST_TIME_ENTRY)).thenReturn(expectedDate)

        repository.loadDate()

        assertEquals(expectedDate, repository.displayDate)
    }

    @Test
    fun `getLastScreen returns last saved screen`() = runTest(testDispatcher) {
        val expectedScreen = "LastScreen"
        `when`(dataStorageHelper.getValue<String>(PreferenceKey.SCREEN)).thenReturn(expectedScreen)

        val result = repository.getLastScreen()

        assertEquals(expectedScreen, result)
    }

    @Test
    fun `getFavoriteTrackIds returns favorite track ids`() = runTest(testDispatcher) {
        val expectedIds = listOf(1L, 2L)
        `when`(database.iTunesFavoriteDao().allIds()).thenReturn(flowOf(expectedIds))

        val result = repository.getFavoriteTrackIds()

        result.collect {
            assertEquals(expectedIds, it)
        }
    }
}
