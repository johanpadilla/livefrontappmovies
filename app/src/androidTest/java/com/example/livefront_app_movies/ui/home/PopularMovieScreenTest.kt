package com.example.livefront_app_movies.ui.home

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.livefront_app_movies.MainActivity
import com.example.livefront_app_movies.ui.theme.LivefrontappmoviesTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class PopularMovieScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()

        composeRule.activity.setContent {
            val navController = rememberNavController()
            LivefrontappmoviesTheme {
                NavHost(navController = navController, startDestination = PopularMovieScreen) {
                    composable<PopularMovieScreen> {
                        PopularMovieScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun activityShouldBeLaunchedAndVisible() {
        composeRule.onNodeWithTag("popular_movie_container").assertExists()
    }

    @Test
    fun activityShouldDisplayError() {
        composeRule.onNodeWithTag("popular_movie_loading_container").assertExists()
    }
}