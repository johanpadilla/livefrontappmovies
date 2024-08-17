package com.example.livefront_app_movies.utils

import com.example.livefront_app_movies.BuildConfig
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun String.toFullPosterURL() = "${URL.POSTER_URL}$this"

/**
 * String formatter extension, it will format a provided date into: Month Day of the Month, year.
 * Example: Jul 09, 2024
 * @return String
 */
fun String.formatDateToMonthAndYear(): String {
    return LocalDate.parse(this).format(LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        dayOfMonth()
        chars(", ")
        year()
    })
}

object URL {
    const val POSTER_URL = BuildConfig.IMAGE_URL
}