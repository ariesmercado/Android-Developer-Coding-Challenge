package com.ariesmercado.itunesmasterdetail.presenter.details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ariesmercado.itunesmasterdetail.R
import com.ariesmercado.itunesmasterdetail.common.DateUtil.formatDate
import com.ariesmercado.itunesmasterdetail.common.constant.Constants
import com.ariesmercado.itunesmasterdetail.common.constant.Constants.MOVIE_DETAILS
import com.ariesmercado.itunesmasterdetail.common.constant.Constants.PREVIEW_URL
import com.ariesmercado.itunesmasterdetail.common.constant.Constants.TERM
import com.ariesmercado.itunesmasterdetail.common.constant.Constants.TRACK_URL
import com.ariesmercado.itunesmasterdetail.data.source.remote.model.Movie
import com.ariesmercado.itunesmasterdetail.presenter.player.ItunesPlayerActivity
import com.ariesmercado.itunesmasterdetail.presenter.webview.ItunesWebViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItunesMovieDetailsActivity : ComponentActivity() {

    private val viewModel: ItunesMovieDetailsViewModel by viewModels()

    var searchTerm: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.MOVIE, Movie::class.java)
        } else {
            intent.getParcelableExtra<Movie>(Constants.MOVIE)
        }

        viewModel.saveScreenState(MOVIE_DETAILS, movie)

        searchTerm = intent.getStringExtra(TERM)

        movie?.let {
            setContent {
                val favorites by viewModel.favoriteMovies.collectAsState()
                MovieDetailsScreen(it, favorites, ::toggleFavorite, ::onWatchPreviewClicked, ::onVisitWebsiteClicked)
            }
        } ?: finish()
    }

    private fun onWatchPreviewClicked(url: String) {
        val intent = Intent(this, ItunesPlayerActivity::class.java)
        intent.putExtra(PREVIEW_URL, url)
        startActivity(intent)
    }

    private fun onVisitWebsiteClicked(url: String) {
        val intent = Intent(this, ItunesWebViewActivity::class.java)
        intent.putExtra(TRACK_URL, url)
        startActivity(intent)
    }

    private fun toggleFavorite(movie: Movie, isFavorite: Boolean) {
        viewModel.toggleFavorite(movie, isFavorite, searchTerm ?: "-")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movie: Movie,
    favorites: List<Long>,
    onToggleFavorite: (Movie, Boolean) -> Unit,
    onWatchPreviewClicked: (String) -> Unit,
    onVisitWebsiteClicked: (String) -> Unit
) {
    val isFavorite = favorites.any { movie.trackId in favorites }

    Column(modifier = Modifier.fillMaxSize()) {
        MovieDetailsTopBar()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                MovieHeader(
                    movie = movie,
                    isFavorite = isFavorite,
                    onToggleFavorite = onToggleFavorite,
                    onWatchPreviewClicked = onWatchPreviewClicked,
                    onVisitWebsiteClicked = onVisitWebsiteClicked
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                MovieDetailsContent(movie)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsTopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.movie_details),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        modifier = Modifier.shadow(8.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun MovieHeader(
    movie: Movie,
    isFavorite: Boolean,
    onToggleFavorite: (Movie, Boolean) -> Unit,
    onWatchPreviewClicked: (String) -> Unit,
    onVisitWebsiteClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
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
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.baseline_photo)
        )

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = movie.trackName.orEmpty(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { onToggleFavorite.invoke(movie, isFavorite) }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) stringResource(R.string.remove_from_favorites) else stringResource(
                            R.string.add_to_favorites
                        ),
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { movie.previewUrl?.let { onWatchPreviewClicked(it) } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.watch_trailer))
            }

            OutlinedButton(
                onClick = { movie.trackViewUrl?.let { onVisitWebsiteClicked(it) } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.visit_website))
            }
        }
    }
}

@Composable
fun MovieDetailsContent(movie: Movie) {
    movie.artistName?.let { MovieDetailItem(label = stringResource(R.string.artist), value = it) }
    MovieDetailItem(label = "Release Date", value = formatDate(movie.releaseDate.orEmpty()))
    movie.primaryGenreName?.let { MovieDetailItem(label = stringResource(R.string.genre), value = it) }
    MovieDetailItem(
        label = "Price",
        value = "${movie.currency} ${movie.trackPrice}",
        valueColor = Color.Green
    )
    Spacer(modifier = Modifier.height(16.dp))
    movie.longDescription?.let { MovieDetailItem(label = stringResource(R.string.description), value = it) }
}


@Composable
fun MovieDetailItem(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

