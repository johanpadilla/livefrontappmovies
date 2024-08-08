package com.example.livefront_app_movies.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.livefront_app_movies.R
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.livefront_app_movies.ui.theme.LocalCustomColorsPalette
import com.example.livefront_app_movies.ui.theme.bodyBold
import com.example.livefront_app_movies.ui.theme.titleLarge
import com.example.livefront_app_movies.ui.util.CenteredMessage
import com.example.livefront_app_movies.utils.formatDateToMonthAndYear
import com.example.livefront_app_movies.utils.toFullPosterURL
import com.example.livefront_app_movies.utils.toMoneyFormat
import kotlinx.serialization.Serializable

/**
 * Movie details screen.
 * It will handle all the details UI elements of a provided movie id.
 * @param navController - To going back.
 * @param movieId - MovieId to be fetched.
 * @param viewModel - MovieDetailViewModel to handle all the business logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    navController: NavController,
    movieId: String?,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    viewModel.getMovieDetail(movieId)
    val movieDetailState = viewModel.detail.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.movie_details_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.go_back_accessibility)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = movieDetailState.value is MovieDetailState.Loading,
            onRefresh = {
                viewModel.onRefresh(movieId)
            },
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    end = dimensionResource(
                        id = R.dimen.smallHorizontalPadding
                    ),
                    start = dimensionResource(id = R.dimen.smallHorizontalPadding),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {
            Content(movieDetailState = movieDetailState.value)
        }

    }
}

@Composable
private fun Content(movieDetailState: MovieDetailState) {
    when (movieDetailState) {
        is MovieDetailState.Loaded -> {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallVerticalPadding))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallHorizontalPadding))
                ) {
                    MovieDetailPoster(url = movieDetailState.movieDetail?.fullPosterPath)
                    Column {
                        val releasedDate =
                            movieDetailState.movieDetail?.releaseDate?.let { "(${it.formatDateToMonthAndYear()})" }
                        Title(movieDetailState.movieDetail?.originalTitle, releasedDate)

                        Genres(genres = movieDetailState.movieDetail?.genres)

                        if (movieDetailState.movieDetail?.spokenLanguages != null && movieDetailState.movieDetail.spokenLanguages.isNotEmpty()) {
                            SpokenLanguages(movieDetailState.movieDetail.spokenLanguages)
                        }
                        if (movieDetailState.movieDetail?.budget != null && movieDetailState.movieDetail.budget > 0) {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
                            Text(text = "${stringResource(id = R.string.budget_text)}: $${movieDetailState.movieDetail.budget.toMoneyFormat()}")
                        }

                        movieDetailState.movieDetail?.status.let {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
                            Text(text = "${stringResource(id = R.string.status_text)}: $it")
                        }
                    }

                }

                movieDetailState.movieDetail?.overview?.let { overview ->
                    val synopsis = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${stringResource(id = R.string.synopsis_text)} ")
                        }
                        append(overview)
                    }
                    Text(text = synopsis)
                }

                movieDetailState.movieDetail?.productionCompanies?.let { companies ->
                    if (companies.any { it.logoPath != null }) ProductionCompany(companies = companies)
                }

                movieDetailState.movieDetail?.productionCountries?.let { countries ->
                    ProductionCountry(countries = countries)
                }
            }
        }

        is MovieDetailState.Loading -> CenteredMessage(message = stringResource(id = R.string.loading_text_message))
        is MovieDetailState.Empty -> CenteredMessage(message = stringResource(id = R.string.empty_text_message))
        is MovieDetailState.Error -> CenteredMessage(message = stringResource(id = R.string.error_text_message))
    }
}

@Composable
private fun SpokenLanguages(spokenLanguages: List<SpokenLanguage>) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
    Text(text = "${stringResource(id = R.string.available_language_text)}:")
    spokenLanguages.map {
        it.englishName?.let { language ->
            BulletList(text = language)
        }
    }
}

@Composable
private fun ProductionCountry(countries: List<ProductionCountry>) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
    Column {
        Text(text = "${stringResource(id = R.string.production_countries_text)}:", style = bodyBold)
        countries.map {
            it.name?.let { country ->
                BulletList(text = country)
            }
        }
    }
}

@Composable
private fun ProductionCompany(companies: List<ProductionCompany>) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
    Column {
        Text(text = "${stringResource(id = R.string.production_companies_text)}:", style = bodyBold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallHorizontalPadding))) {
            items(companies.size) { index ->
                if (companies[index].logoPath.isNullOrEmpty().not()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(companies[index].logoPath?.toFullPosterURL())
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.rowImageHeight))
                            .background(color = LocalCustomColorsPalette.current.productionCompaniesBackgroundColor)
                            .padding(dimensionResource(id = R.dimen.smallHorizontalPadding))
                    )
                }
            }
        }
    }
}

@Composable
private fun Genres(genres: List<Genre>?) {
    genres?.map {
        it.name?.let { genre ->
            BulletList(text = genre)
        }
    }
}

@Composable
private fun Title(originalTitle: String?, releasedDate: String?) {
    originalTitle?.let {
        Text(
            text = "$it $releasedDate",
            textAlign = TextAlign.Center,
            style = titleLarge
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
    }
}

@Composable
private fun BulletList(text: String) {
    Row {
        Text(text = stringResource(id = R.string.bullet))
        Text(text = text)
    }
}

@Composable
private fun MovieDetailPoster(url: String?) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url).crossfade(true)
            .build(),
        contentDescription = null,
    )
}


@Serializable
data class MovieDetailsScreen(val id: String?)