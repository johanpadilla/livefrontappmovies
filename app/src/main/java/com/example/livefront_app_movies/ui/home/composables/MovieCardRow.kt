package com.example.livefront_app_movies.ui.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.livefront_app_movies.R
import com.example.livefront_app_movies.ui.home.PopularMovie

/**
 * Row which will render movie card items,
 */
@Composable
fun MovieCardRow(movieList: List<PopularMovie>, onPopularMovieClicked: (String) -> Unit) {
    Row(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.smallHorizontalPadding)),
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.smallHorizontalPadding)
        )
    ) {
        movieList.map { movie ->
            MovieCard(
                popularMovie = movie,
                onPopularMovieClick = { movieId ->
                    onPopularMovieClicked(movieId)
                }
            )
        }
    }
}