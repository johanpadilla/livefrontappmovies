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
import com.example.livefront_app_movies.ui.details.DetailViewModel
import com.example.livefront_app_movies.ui.details.MovieDetailScreen
import com.example.livefront_app_movies.ui.details.MovieDetailsScreen
import com.example.livefront_app_movies.ui.home.HomeScreen
import com.example.livefront_app_movies.ui.home.HomeViewModel
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
                    startDestination = HomeScreen
                ) {
                    composable<HomeScreen> {
                        val homeViewModel: HomeViewModel by viewModels()
                        HomeScreen(navController = navController, viewModel = homeViewModel)
                    }
                    composable<MovieDetailsScreen> {
                        val detailsViewModel: DetailViewModel by viewModels()
                        val id = it.toRoute<MovieDetailsScreen>()
                        MovieDetailScreen(navController, id.id, detailsViewModel)
                    }
                }
            }
        }
    }
}