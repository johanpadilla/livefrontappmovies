package com.example.livefront_app_movies.ui.flow

import kotlinx.coroutines.flow.SharingStarted

interface SharingRestartable: SharingStarted {
    fun restart()
}