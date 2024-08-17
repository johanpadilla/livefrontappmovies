package com.example.livefront_app_movies.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.livefront_app_movies.BuildConfig
import com.example.livefront_app_movies.R
import com.example.livefront_app_movies.ui.home.composables.MovieCardRow
import com.example.livefront_app_movies.ui.util.CenteredMessage
import com.example.livefront_app_movies.ui.util.PullToRefreshContainer

const val POPULAR_MOVIE_COLUMNS = 2

/**
 * Principal screen, which provides a list of popular movies fetched by the API.
 * @param onPopularMovieClick - To navigate to the movie details screen.
 * @param viewModel - To handle all the business logic.
 */
@Composable
fun PopularMovieScreen(
    viewModel: PopularMovieViewModel = hiltViewModel(),
    onPopularMovieClick: (String) -> Unit
) {
    val moviesState by viewModel.popularMovies.collectAsStateWithLifecycle(lifecycle = LocalLifecycleOwner.current.lifecycle)

    PopularMovieScreenContent(
        movieState = moviesState,
        onPopularMovieClick = onPopularMovieClick
    ) { onError -> viewModel.refresh(onError) }
    LaunchedEffect(Unit) {
        viewModel.getPopularMovies()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PopularMovieScreenContent(
    movieState: PopularMovieState,
    onPopularMovieClick: (String) -> Unit,
    onRefresh: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.home_screen_title))
                }
            )
        },

        ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Content(movieState, onPopularMovieClick, onRefresh)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    movieState: PopularMovieState,
    onPopularMovieClick: (String) -> Unit,
    onRefresh: (onError: Boolean) -> Unit
) {
    when (movieState) {
        is PopularMovieState.Loaded -> {
            PullToRefreshContainer(
                isRefreshing = movieState.isRefreshing,
                onRefresh = { onRefresh(false) },
                content = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val chunkedList = movieState.movies.chunked(POPULAR_MOVIE_COLUMNS)
                        items(chunkedList.size) { index ->
                            MovieCardRow(
                                movieList = chunkedList[index]
                            ) { movieId -> onPopularMovieClick(movieId) }
                        }
                    }
                }
            )
        }

        is PopularMovieState.Loading -> CenteredMessage(
            modifier = Modifier.testTag("popular_movie_loading_container"),
            message = stringResource(id = R.string.loading_text_message)
        )

        is PopularMovieState.Empty -> {
            PullToRefreshContainer(
                isRefreshing = false,
                onRefresh = { onRefresh(true) },
                content = {
                    CenteredMessage(message = stringResource(id = R.string.empty_text_message))
                }
            )
        }

        is PopularMovieState.Error -> {
            PullToRefreshContainer(
                isRefreshing = false,
                onRefresh = { onRefresh(true) },
                content = {
                    CenteredMessage(
                        modifier = Modifier.testTag("popular_movie_error_container"),
                        message = "${stringResource(id = R.string.error_text_message)} ${
                            if (BuildConfig.ACCESS_TOKEN.isEmpty()) stringResource(
                                id = R.string.check_the_token_text
                            ) else ""
                        }"
                    )
                }
            )
        }
    }
}