/*
 *abiola 2022
 */

package com.mshdabiola.skeletonandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.mshdabiola.detail.navigation.MAIN_ROUTE
import com.mshdabiola.detail.navigation.detailScreen
import com.mshdabiola.detail.navigation.mainScreen
import com.mshdabiola.detail.navigation.navigateToDetail
import com.mshdabiola.skeletonandroid.ui.SkAppState

@Composable
fun SkNavHost(
    appState: SkAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = MAIN_ROUTE,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        mainScreen(onShowSnackbar = onShowSnackbar, onClicked = navController::navigateToDetail)
        detailScreen(onShowSnackbar, navController::popBackStack)
    }
}
