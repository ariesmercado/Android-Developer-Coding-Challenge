package com.ariesmercado.itunesmasterdetail.presenter.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ariesmercado.itunesmasterdetail.R
import com.ariesmercado.itunesmasterdetail.common.Resource
import com.ariesmercado.itunesmasterdetail.common.constant.Constants.ITUNES_LIST
import com.ariesmercado.itunesmasterdetail.common.constant.Constants.MOVIE
import com.ariesmercado.itunesmasterdetail.common.constant.Constants.TERM
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesFavorites
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.toMovie
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.ItunesResponse
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.Movie
import com.ariesmercado.itunesmasterdetail.presenter.details.ItunesMovieDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItunesListActivity : ComponentActivity() {

    private val viewModel: ItunesListViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.iTunesState.collectAsState()
            val favoritesIds by viewModel.favoriteIds.collectAsState()

            val favorites by viewModel.favoriteMovies.collectAsState()

            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.last_visit, viewModel.displayDate),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    modifier = Modifier.shadow(8.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
                MainScreen(
                    state,
                    favoritesIds,
                    favorites,
                    ::onSearch,
                    ::navigateMovieDetails,
                    ::toggleFavorite
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.saveScreenState(ITUNES_LIST)
    }

    private fun onSearch(term: String) {
        viewModel.search(term)
    }

    private fun navigateMovieDetails(movie: Movie, term: String) {
        val intent = Intent(this, ItunesMovieDetailsActivity::class.java)
        intent.putExtra(MOVIE, movie)
        intent.putExtra(TERM, term)
        startActivity(intent)
    }

    private fun toggleFavorite(movie: Movie, isFavorite: Boolean, term: String) {
        viewModel.toggleFavorite(movie, isFavorite, term)
    }
}

@Composable
fun MainScreen(
    state: Resource<ItunesResponse>,
    favoriteIds: List<Long>,
    favorites: List<ItunesFavorites>,
    onSearch: (String) -> Unit,
    onItemClicked: (Movie, String) -> Unit,
    onFavoriteClicked: (Movie, Boolean, String) -> Unit,
) {
    val selectedTab = remember { mutableIntStateOf(0) }
    val searchText = remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        when (selectedTab.intValue) {
            0 -> MovieSearchScreen(
                state = state,
                favoritesIds = favoriteIds,
                searchText = searchText,
                onSearch = { onSearch.invoke(it) },
                onItemClicked = { onItemClicked.invoke(it, searchText.value) },
                onFavoriteClicked = { movie, isFavorite, term -> onFavoriteClicked(movie, isFavorite, term) }
            )
            1 -> FavoritesScreen(
                favorites = favorites,
                favoritesIds = favoriteIds,
                searchText = searchText.value,
                onItemClicked = { onItemClicked.invoke(it, searchText.value) },
                onFavoriteClicked = { movie, isFavorite, term -> onFavoriteClicked(movie, isFavorite, term) }
            )
        }

        BottomNavigationBar(
            selectedTab = selectedTab,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        BackHandler {
            if (selectedTab.intValue == 1) {
                selectedTab.intValue = 0
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: MutableState<Int>, modifier: Modifier) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search)) },
            label = { Text(stringResource(R.string.search)) },
            selected = selectedTab.value == 0,
            onClick = { selectedTab.value = 0 },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = stringResource(R.string.favorites)) },
            label = { Text("Favorites") },
            selected = selectedTab.value == 1,
            onClick = { selectedTab.value = 1 },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )
    }
}

@Composable
fun MovieSearchScreen(
    state: Resource<ItunesResponse>,
    favoritesIds: List<Long>,
    onSearch: (String) -> Unit,
    onItemClicked: (Movie) -> Unit,
    onFavoriteClicked: (Movie, Boolean, String) -> Unit,
    searchText: MutableState<String>,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            SearchBar(searchText.value) {
                searchText.value = it
                onSearch.invoke(it)
            }


            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                is Resource.Loading -> if (searchText.value.isNotBlank()) ContentLoading() else {
                    ContentResultMessage(stringResource(R.string.search_box_cannot_be_empty_enter_a_term_to_find_results))
                }

                is Resource.Success -> {
                    if (searchText.value.isNotBlank()) {
                        if (!state.data?.results.isNullOrEmpty()) {
                            state.data?.results?.let {
                                MovieList(
                                    movies = it,
                                    favoritesIds,
                                    onItemClicked,
                                    onFavoriteClicked,
                                    searchText.value
                                )
                            }
                        } else {
                            ContentResultMessage(stringResource(R.string.no_result_found))
                        }
                    }
                }

                is Resource.Error -> {
                    ContentResultMessage("Error:${state.message}")
                }
            }
        }
    }
}

@Composable
fun SearchBar(searchText: String, onSearchTextChanged: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { focusManager.clearFocus() },
        label = { Text(text = stringResource(R.string.search_movies)) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search_icon))
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearchTextChanged.invoke(searchText)
                focusManager.clearFocus()
            }
        )
    )
}


@Composable
fun MovieList(
    movies: List<Movie>,
    favoritesIds: List<Long>,
    onItemClicked: (Movie) -> Unit,
    onFavoriteClicked: (Movie, Boolean, String) -> Unit,
    searchText: String
) {
    val savedMovies = rememberSaveable { mutableStateOf(movies) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(savedMovies.value) { movie ->
            MovieItem(movie = movie, favoritesIds, onItemClicked, onFavoriteClicked, searchText)
        }
        item {
            Spacer(modifier = Modifier.height(250.dp))
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    favoritesIds: List<Long>,
    onItemClicked: (Movie) -> Unit,
    onFavoriteClicked: (Movie, Boolean, String) -> Unit,
    searchText: String,
) {
    val isFavorite = favoritesIds.any { it == movie.trackId }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clickable { onItemClicked.invoke(movie) },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.artworkUrl100)
                    .crossfade(true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = movie.trackName,
                modifier = Modifier
                    .fillMaxWidth(.3f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillWidth,
                error = painterResource(id = R.drawable.baseline_photo)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = movie.trackName.orEmpty(),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Price: ${movie.trackPrice}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = "Genre: ${movie.primaryGenreName}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = { onFavoriteClicked.invoke(movie, isFavorite, searchText) }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun FavoritesScreen(
    favorites: List<ItunesFavorites>,
    favoritesIds: List<Long>,
    searchText: String,
    onItemClicked: (Movie) -> Unit,
    onFavoriteClicked: (Movie, Boolean, String) -> Unit,
    ) {
    Column {
        val savedMovies = rememberSaveable { mutableStateOf(favorites) }

        if (favorites.isNotEmpty()) {
            Text(
                text = stringResource(R.string.favorite_movies),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(savedMovies.value) { item ->
                    MovieItem(movie = item.toMovie(), favoritesIds, onItemClicked, onFavoriteClicked, searchText)
                }

                item {
                    Spacer(modifier = Modifier.height(250.dp))
                }
            }
        } else {
            ContentResultMessage(message = stringResource(R.string.no_favorites_added_yet))
        }
    }
}

@Composable
fun ContentResultMessage(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ContentLoading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}




