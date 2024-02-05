/*
 *abiola 2022
 */

package com.mshdabiola.data.repository.fake

import com.mshdabiola.data.repository.UserDataRepository
import com.mshdabiola.datastore.SimpleNotePreferencesDataSource
import com.mshdabiola.model.DarkThemeConfig
import com.mshdabiola.model.ThemeBrand
import com.mshdabiola.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Fake implementation of the [UserDataRepository] that returns hardcoded user data.
 *
 * This allows us to run the app with fake data, without needing an internet connection or working
 * backend.
 */
class FakeUserDataRepository @Inject constructor(
    private val simpleNotePreferencesDataSource: SimpleNotePreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        simpleNotePreferencesDataSource.userData

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        simpleNotePreferencesDataSource.setThemeBrand(themeBrand)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        simpleNotePreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        simpleNotePreferencesDataSource.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        simpleNotePreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
    }
}
