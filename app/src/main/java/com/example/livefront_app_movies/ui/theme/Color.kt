package com.example.livefront_app_movies.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorPalette(
    val productionCompaniesBackgroundColor: Color = Color.Unspecified,

    )

val LightCustomPalette = ColorPalette(
    productionCompaniesBackgroundColor= Color.White,

)

val DarkCustomPalette = ColorPalette(
    productionCompaniesBackgroundColor = Color.DarkGray,
)


val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


val LocalCustomColorsPalette = staticCompositionLocalOf { ColorPalette() }