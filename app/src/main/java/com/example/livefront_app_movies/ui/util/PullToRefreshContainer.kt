package com.example.livefront_app_movies.ui.util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Wrapper for a PullToRefreshBox.
 * @param modifier Modifier for the PullToRefreshBox container,
 * @param isRefreshing boolean which indicates if a refresh state is performing.
 * @param onRefresh function which will be executed when a Pull-to-Refresh occurs.
 * @param content composable content to be displayed inside the PullToRefresh container.
 * @param state rememberPullToRefreshState
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshContainer(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
    state: PullToRefreshState = rememberPullToRefreshState()
) {
    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = state
    ) {
        content()
    }
}