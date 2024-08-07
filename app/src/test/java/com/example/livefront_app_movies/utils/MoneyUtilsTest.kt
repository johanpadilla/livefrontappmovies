package com.example.livefront_app_movies.utils

import org.junit.Test

class MoneyUtilsTest {

    @Test
    fun usingExtensionFunctionShouldFormatMoney() {
        val moneyToFormat = 1000
        val expected = "1,000"
        val result = moneyToFormat.toMoneyFormat()
        assert(result == expected)
    }
}