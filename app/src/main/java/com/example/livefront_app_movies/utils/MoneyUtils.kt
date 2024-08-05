package com.example.livefront_app_movies.utils

import java.text.NumberFormat
import java.util.Locale

fun Int.toMoneyFormat(): String = NumberFormat.getNumberInstance(Locale.US).format(this)