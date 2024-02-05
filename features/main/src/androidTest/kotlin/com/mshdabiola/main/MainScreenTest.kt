/*
 *abiola 2022
 */

package com.mshdabiola.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.mshdabiola.ui.MainNoteUiState
import com.mshdabiola.ui.MainUiState
import com.mshdabiola.ui.NoteUiState
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for [MainScreen] composable.
 */
class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun enterText_showsShowText() {
        composeTestRule.setContent {
            MainScreen(
                mainState = com.mshdabiola.main.MainUiState.Success(
                    noteUiStates = listOf(
                        MainNoteUiState(
                            id = 6970L,
                            title = "Tonia",
                            content = null,
                            checkFraction = null,
                            path = null,
                            createAt = "Chanse"
                        )
                    ).toImmutableList()
                ),
                searchNotes = emptyList<MainNoteUiState>().toImmutableList()
            )
        }

        composeTestRule
            .onNodeWithTag("main:list")
            .assertExists()
    }
}
