/*
 *abiola 2022
 */

package com.mshdabiola.simplenote.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.mshdabiola.detail.navigation.detailScreen
import com.mshdabiola.detail.navigation.navigateToDetail
import com.mshdabiola.main.navigation.MAIN_ROUTE
import com.mshdabiola.main.navigation.mainScreen
import com.mshdabiola.simplenote.ui.SimpleNoteAppState

@Composable
fun SkNavHost(
    appState: SimpleNoteAppState,
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
        mainScreen(onClicked = navController::navigateToDetail)
        detailScreen(onShowSnackbar, navController::popBackStack)
    }
}
