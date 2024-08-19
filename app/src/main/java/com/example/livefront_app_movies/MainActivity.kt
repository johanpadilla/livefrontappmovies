package com.example.livefront_app_movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.livefront_app_movies.navigation.Argument
import com.example.livefront_app_movies.navigation.Screen
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
                    startDestination = Screen.POPULAR_MOVIE
                ) {
                    composable(
                        route = Screen.POPULAR_MOVIE
                    ) {
                        PopularMovieScreen() {
                            navController.navigate("${Screen.MOVIE_DETAILS}/$it")
                        }
                    }
                    composable(
                        route = "${Screen.MOVIE_DETAILS}/{${Argument.MOVIE_ID_KEY}}",
                        arguments = listOf(
                            navArgument(Argument.MOVIE_ID_KEY){
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