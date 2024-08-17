package com.example.livefront_app_movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.livefront_app_movies.navigation.Destinations
import com.example.livefront_app_movies.ui.details.MovieDetailScreen
import com.example.livefront_app_movies.ui.home.PopularMovieScreen
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
                    startDestination = Destinations.PopularMovie.route
                ) {
                    composable(Destinations.PopularMovie.route) {
                        PopularMovieScreen(){
                            navController.navigate("movie_detail/$it")
                        }
                    }
                    composable(Destinations.MovieDetail.route) {
                        val movieId = it.arguments?.getString("id")
                            ?.let(::requireNotNull)
                            .orEmpty()
                        MovieDetailScreen({navController.navigateUp()}, movieId)
                    }
                }
            }
        }
    }
}