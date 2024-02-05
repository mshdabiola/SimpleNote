/*
 *abiola 2022
 */

package com.mshdabiola.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.mshdabiola.detail.DetailScreen
import com.mshdabiola.ui.NoteUiState
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `title_text-field_exist`() {
        composeTestRule.setContent {
            DetailScreen(currentNoteUiState = NoteUiState(1))
        }
        composeTestRule
            .onNodeWithTag("detail:title")

            .assertExists()
    }
}
