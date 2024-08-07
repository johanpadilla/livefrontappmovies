package com.example.livefront_app_movies.utils

import org.junit.Test

class StringUtilsTest {

    @Test
    fun shouldFormatCorrectDate() {
        val expectedDate = "Jul 09, 1995"
        val dateToFormat = "1995-07-09"
        val result = dateToFormat.formatDateToMonthAndYear()
        assert(result.equals(expectedDate))
    }
}