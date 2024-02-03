/*
 *abiola 2024
 */

package com.mshdabiola.datastore

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SimpleNotePreferencesDataSourceTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: SimpleNotePreferencesDataSource

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        subject = SimpleNotePreferencesDataSource(
            tmpFolder.testUserPreferencesDataStore(testScope),
        )
    }

    @Test
    fun shouldHideOnboardingIsFalseByDefault() = testScope.runTest {
        assertFalse(subject.userData.first().shouldHideOnboarding)
    }

    @Test
    fun userShouldHideOnboardingIsTrueWhenSet() = testScope.runTest {
        subject.setShouldHideOnboarding(true)
        assertTrue(subject.userData.first().shouldHideOnboarding)
    }

    @Test
    fun shouldUseDynamicColorFalseByDefault() = testScope.runTest {
        assertFalse(subject.userData.first().useDynamicColor)
    }

    @Test
    fun userShouldUseDynamicColorIsTrueWhenSet() = testScope.runTest {
        subject.setDynamicColorPreference(true)
        assertTrue(subject.userData.first().useDynamicColor)
    }
}
