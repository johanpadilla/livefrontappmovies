package com.example.livefront_app_movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.livefront_app_movies.ui.details.MovieDetailViewModel
import com.example.livefront_app_movies.ui.details.MovieDetailScreen
import com.example.livefront_app_movies.ui.details.MovieDetailsScreen
import com.example.livefront_app_movies.ui.home.PopularMovieScreen
import com.example.livefront_app_movies.ui.home.PopularMovieViewModel
import com.example.livefront_app_movies.ui.theme.LivefrontappmoviesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LivefrontappmoviesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = PopularMovieScreen
                ) {
                    composable<PopularMovieScreen> {
                        val popularMovieViewModel: PopularMovieViewModel by viewModels()
                        PopularMovieScreen(navController = navController, viewModel = popularMovieViewModel)
                    }
                    composable<MovieDetailsScreen> {
                        val detailsViewModel: MovieDetailViewModel by viewModels()
                        val id = it.toRoute<MovieDetailsScreen>()
                        MovieDetailScreen(navController, id.id, detailsViewModel)
                    }
                }
            }
        }
    }
}