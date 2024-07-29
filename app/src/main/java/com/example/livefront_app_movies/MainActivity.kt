package com.example.livefront_app_movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.livefront_app_movies.ui.theme.LivefrontappmoviesTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LivefrontappmoviesTheme {
                val navController = rememberNavController()
                NavHost(navController = navController,
                    startDestination = ScreenA) {
                    composable<ScreenA> {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                            Button(onClick = { navController.navigate(ScreenB("123")) }) {
                                Text("Go to screen B")
                            }
                        }
                    }
                    composable<ScreenB> {
                        val id = it.toRoute<ScreenB>()
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                            Text("Id send: ${id.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LivefrontappmoviesTheme {
        Greeting("Android")
    }
}

@Serializable
object ScreenA

@Serializable
data class ScreenB(val id: String)