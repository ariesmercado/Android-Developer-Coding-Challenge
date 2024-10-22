package com.ariesmercado.itunesmasterdetail.presenter.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.ariesmercado.itunesmasterdetail.R
import com.ariesmercado.itunesmasterdetail.common.constant.Constants.MOVIE
import com.ariesmercado.itunesmasterdetail.common.constant.Constants.MOVIE_DETAILS
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.ItunesLastMovie
import com.ariesmercado.itunesmasterdetail.data.source.local.entity.toMovie
import com.ariesmercado.itunesmasterdetail.presenter.details.ItunesMovieDetailsActivity
import com.ariesmercado.itunesmasterdetail.presenter.search.ItunesListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val movieState by viewModel.lastMovie.collectAsState()
            SplashScreen {
                viewModel.loadDate()

                navigateToMainScreen(movieState)
            }
        }
    }

    private fun navigateToMainScreen(movieState: ItunesLastMovie?) {
        lifecycleScope.launch {
            val lastScreen = viewModel.getLastScreen()
            delay(2000)

            when (lastScreen) {
                MOVIE_DETAILS -> {
                    movieState?.let { lastMovie ->
                        val intent = Intent(this@SplashScreenActivity, ItunesMovieDetailsActivity::class.java)
                        intent.putExtra(MOVIE, lastMovie.toMovie())
                        startActivity(intent)
                    } ?: run {
                        val intent = Intent(this@SplashScreenActivity, ItunesListActivity::class.java)
                        startActivity(intent)
                    }
                }
                else  -> {
                    val intent = Intent(this@SplashScreenActivity, ItunesListActivity::class.java)
                    startActivity(intent)
                }
            }
            finish()
        }
    }

    @Composable
    fun SplashScreen(onTimeout: () -> Unit) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            // Vertically centered content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                BasicText(
                    text = stringResource(R.string.itunes),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                )

                // Subtitle
                BasicText(
                    text = stringResource(R.string.master_detail),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 20.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        onTimeout()

    }

    @Preview(showBackground = true)
    @Composable
    fun SplashScreenPreview() {
        SplashScreen(onTimeout = {})
    }
}
