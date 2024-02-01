/*
 *abiola 2022
 */

package com.mshdabiola.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mshdabiola.detail.MainRoute

const val MAIN_ROUTE = "main_route"

fun NavController.navigateToMain(navOptions: NavOptions) = navigate(MAIN_ROUTE, navOptions)

fun NavGraphBuilder.mainScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onClicked: (Long) -> Unit,
) {
    composable(route = MAIN_ROUTE) {
        MainRoute(onClick = onClicked, onShowSnackbar)
    }
}
