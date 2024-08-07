package com.example.livefront_app_movies.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.livefront_app_movies.BuildConfig
import com.example.livefront_app_movies.R
import com.example.livefront_app_movies.ui.details.MovieDetailsScreen
import com.example.livefront_app_movies.ui.util.CenteredMessage
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

const val POPULAR_MOVIE_COLUMNS = 2

/**
 * Principal screen, which provides a list of popular movies fetched by the API.
 * @param navController - To navigate between screens.
 * @param viewModel - To handle all the business logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularMovieScreen(navController: NavController, viewModel: PopularMovieViewModel) {
    val moviesState = viewModel.movies.collectAsState().value
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.home_screen_title))
                }
            )
        },

    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
            ,
            isRefreshing = viewModel.movies.collectAsState().value is PopularMovieState.Loading,
            onRefresh = {
                scope.launch {
                    viewModel.restart()
                }
            },
            content = {
                when (moviesState) {

                    is PopularMovieState.Loaded -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val chunkedList = moviesState.movies.chunked(POPULAR_MOVIE_COLUMNS)
                            items(chunkedList.size) { index ->
                                if (chunkedList[index].size > POPULAR_MOVIE_COLUMNS - 1) {
                                    Row(
                                        modifier = Modifier.padding(dimensionResource(id = R.dimen.smallHorizontalPadding)),
                                        horizontalArrangement = Arrangement.spacedBy(
                                            dimensionResource(id = R.dimen.smallHorizontalPadding)
                                        )
                                    ) {
                                        MovieCard(
                                            popularMovie = chunkedList[index][0],
                                            onPopularMovieClick = {
                                                navController.navigate(
                                                    MovieDetailsScreen(chunkedList[index][0].id.toString())
                                                )
                                            })
                                        MovieCard(
                                            popularMovie = chunkedList[index][1],
                                            onPopularMovieClick = {
                                                navController.navigate(
                                                    MovieDetailsScreen(chunkedList[index][1].id.toString())
                                                )
                                            })
                                    }

                                } else {
                                    MovieCard(
                                        popularMovie = chunkedList[index][0],
                                        onPopularMovieClick = {
                                            navController.navigate(
                                                MovieDetailsScreen(chunkedList[index][0].id.toString())
                                            )
                                        })
                                }
                            }
                        }
                    }

                    is PopularMovieState.Loading -> CenteredMessage(message = stringResource(id = R.string.loading_text_message))
                    is PopularMovieState.Empty -> CenteredMessage(message = stringResource(id = R.string.empty_text_message))
                    is PopularMovieState.Error -> CenteredMessage(
                        message = "${stringResource(id = R.string.error_text_message)} ${
                            if (BuildConfig.TOKEN.isEmpty()) stringResource(
                                id = R.string.check_the_token_text
                            ) else ""
                        }"
                    )
                }
            }
        )
    }
}

@Composable
private fun MovieCard(popularMovie: PopularMovie, onPopularMovieClick: () -> Unit) {
    Card(onClick = { onPopularMovieClick.invoke() }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(popularMovie.fullPosterUrl).crossfade(true)
                .build(),
            contentDescription = null,
        )
    }
}


@Serializable
object PopularMovieScreen
