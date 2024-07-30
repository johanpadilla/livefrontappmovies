package com.example.livefront_app_movies.ui.details

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
import com.example.livefront_app_movies.R
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.livefront_app_movies.ui.theme.bodyBold
import com.example.livefront_app_movies.ui.theme.titleLarge
import com.example.livefront_app_movies.ui.util.CenteredMessage
import com.example.livefront_app_movies.utils.toFullPosterURL
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    navController: NavController,
    movieId: String?,
    viewModel: MovieDetailViewModel
) {
    viewModel.getMovieDetail(movieId)
    val uiState = viewModel.detail.collectAsState().value
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
        when (uiState) {
            is MovieDetailState.Loaded -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            end = dimensionResource(
                                id = R.dimen.smallHorizontalPadding
                            ),
                            start = dimensionResource(id = R.dimen.smallHorizontalPadding)
                        )
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallVerticalPadding))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.smallHorizontalPadding))
                    ) {
                        MovieDetailPoster(url = uiState.movieDetail?.fullPosterPath)
                        Column {
                            val releasedDate = uiState.movieDetail?.releaseDate?.let { "($it)" }
                            Title(uiState.movieDetail?.originalTitle, releasedDate)

                            Genres(genres = uiState.movieDetail?.genres)

                            if (uiState.movieDetail?.spokenLanguages != null && uiState.movieDetail.spokenLanguages.isNotEmpty()) {
                                SpokenLanguages(uiState.movieDetail.spokenLanguages)
                            }

                            uiState.movieDetail?.budget.let {
                                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
                                Text(text = "${stringResource(id = R.string.budget_text)}: $$it")
                            }

                            uiState.movieDetail?.status.let {
                                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.smallHorizontalPadding)))
                                Text(text = "${stringResource(id = R.string.status_text)}: $it")
                            }
                        }

                    }

                    uiState.movieDetail?.overview?.let { overview ->
                        val synopsis = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("${stringResource(id = R.string.synopsis_text)} ")
                            }
                            append(overview)
                        }
                        Text(text = synopsis)
                    }

                    uiState.movieDetail?.productionCompanies?.let { companies ->

                        if (companies.any { it.logoPath != null }) ProductionCompany(companies = companies)

                    }

                    uiState.movieDetail?.productionCountries?.let { countries ->
                        ProductionCountry(countries = countries)
                    }
                }
            }

            is MovieDetailState.Loading -> CenteredMessage(message = stringResource(id = R.string.loading_text_message))
            is MovieDetailState.Empty -> CenteredMessage(message = stringResource(id = R.string.empty_text_message))
            is MovieDetailState.Error -> CenteredMessage(message = stringResource(id = R.string.error_text_message))
        }
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
        LazyRow(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.largeXLHorizontalPadding))) {
            items(companies.size) { index ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(companies[index].logoPath?.toFullPosterURL())
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.rowImageHeight))
                )
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