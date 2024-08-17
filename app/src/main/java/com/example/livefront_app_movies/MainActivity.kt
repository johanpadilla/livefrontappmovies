package com.example.livefront_app_movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
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
                    startDestination = Destinations.POPULAR_MOVIE_SCREEN
                ) {
                    composable(
                        route = Destinations.POPULAR_MOVIE_SCREEN
                    ) {
                        PopularMovieScreen() {
                            navController.navigate("${Destinations.MOVIE_DETAILS_SCREEN}/$it")
                        }
                    }
                    composable(
                        route = "${Destinations.MOVIE_DETAILS_SCREEN}/{${Destinations.MOVIE_DETAILS_SCREEN_KEY}}",
                        arguments = listOf(
                            navArgument(Destinations.MOVIE_DETAILS_SCREEN_KEY){
                                NavType.StringType
                            }
                        )
                    ) {
                        MovieDetailScreen({navController.navigateUp()})
                    }
                }
            }
        }
    }
}