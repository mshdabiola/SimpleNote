/*
 *abiola 2024
 */

package com.mshdabiola.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.accompanist.testharness.TestHarness
import com.mshdabiola.designsystem.component.SkTopAppBar
import com.mshdabiola.designsystem.icon.SimpleNoteIcons
import com.mshdabiola.designsystem.theme.SimpleNoteTheme
import com.mshdabiola.testing.util.DefaultRoborazziOptions
import com.mshdabiola.testing.util.captureMultiTheme
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class TopAppBarScreenshotTests() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun topAppBar_multipleThemes() {
        composeTestRule.captureMultiTheme("TopAppBar") {
            NiaTopAppBarExample()
        }
    }

    @Test
    fun topAppBar_hugeFont() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                TestHarness(fontScale = 2f) {
                    SimpleNoteTheme {
                        NiaTopAppBarExample()
                    }
                }
            }
        }
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/screenshots/TopAppBar/TopAppBar_fontScale2.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }

    @Composable
    private fun NiaTopAppBarExample() {
        SkTopAppBar(
            titleRes = android.R.string.untitled,
            navigationIcon = SimpleNoteIcons.Search,
            navigationIconContentDescription = "Navigation icon",
            actionIcon = SimpleNoteIcons.MoreVert,
            actionIconContentDescription = "Action icon",
        )
    }
}
