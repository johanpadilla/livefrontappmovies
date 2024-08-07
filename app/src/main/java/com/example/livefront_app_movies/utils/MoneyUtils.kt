package com.example.livefront_app_movies.utils

import java.text.NumberFormat
import java.util.Locale

/**
 * Int formatter extension, it will take and Integer value and format it to Locale.US format.
 * @return String
 */
fun Int.toMoneyFormat(): String = NumberFormat.getNumberInstance(Locale.US).format(this)