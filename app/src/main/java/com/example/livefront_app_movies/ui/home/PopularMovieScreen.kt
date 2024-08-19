package com.example.livefront_app_movies.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.livefront_app_movies.BuildConfig
import com.example.livefront_app_movies.R
import com.example.livefront_app_movies.ui.home.composables.MovieCard
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
    val isLandScapeMode =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    PopularMovieScreenContent(
        movieState = moviesState,
        onPopularMovieClick = onPopularMovieClick,
        isLandScapeMode
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
    isLandScapeMode: Boolean,
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
            Content(movieState, isLandScapeMode, onPopularMovieClick, onRefresh)
        }
    }
}

@Composable
private fun LandscapeGrid(movies: List<PopularMovie>, onPopularMovieClick: (String) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.smallHorizontalPadding)),
        columns = GridCells.Adaptive(dimensionResource(id = R.dimen.landscapeMinCellsSize)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallHorizontalPadding)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallHorizontalPadding))
    ) {
        items(
            movies.size
        ) {
            MovieCard(
                modifier = Modifier
                    .wrapContentSize()
                    .height(dimensionResource(id = R.dimen.landscapeCardImageHeight)),
                popularMovie = movies[it],
                onPopularMovieClick = onPopularMovieClick
            )
        }

    }
}

@Composable
private fun PortraitGrid(movies: List<PopularMovie>, onPopularMovieClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(POPULAR_MOVIE_COLUMNS),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallHorizontalPadding)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallHorizontalPadding))
    ) {
        items(
            movies.size
        ) {
            MovieCard(
                modifier = Modifier.wrapContentSize(),
                popularMovie = movies[it],
                onPopularMovieClick = onPopularMovieClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    movieState: PopularMovieState,
    isLandScapeMode: Boolean,
    onPopularMovieClick: (String) -> Unit,
    onRefresh: (onError: Boolean) -> Unit
) {
    when (movieState) {
        is PopularMovieState.Loaded -> {
            PullToRefreshContainer(
                modifier = Modifier.testTag("popular_movie_container"),
                isRefreshing = movieState.isRefreshing,
                onRefresh = { onRefresh(false) },
                content = {
                    if (isLandScapeMode.not()) {
                        PortraitGrid(
                            movies = movieState.movies,
                            onPopularMovieClick = onPopularMovieClick
                        )
                    } else {
                        LandscapeGrid(
                            movies = movieState.movies,
                            onPopularMovieClick = onPopularMovieClick
                        )
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
                modifier = Modifier.testTag("test"),
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