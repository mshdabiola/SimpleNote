/*
 *abiola 2024
 */

package com.mshdabiola.data.repository

import androidx.annotation.VisibleForTesting
import com.mshdabiola.analytics.AnalyticsHelper
import com.mshdabiola.datastore.SkPreferencesDataSource
import com.mshdabiola.model.DarkThemeConfig
import com.mshdabiola.model.ThemeBrand
import com.mshdabiola.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineFirstUserDataRepository @Inject constructor(
    private val skPreferencesDataSource: SkPreferencesDataSource,
    private val analyticsHelper: AnalyticsHelper,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        skPreferencesDataSource.userData

    @VisibleForTesting
    override suspend fun setFollowedTopicIds(followedTopicIds: Set<String>) =
        skPreferencesDataSource.setFollowedTopicIds(followedTopicIds)

    override suspend fun setTopicIdFollowed(followedTopicId: String, followed: Boolean) {
        skPreferencesDataSource.setTopicIdFollowed(followedTopicId, followed)
        analyticsHelper.logTopicFollowToggled(followedTopicId, followed)
    }

    override suspend fun updateNewsResourceBookmark(newsResourceId: String, bookmarked: Boolean) {
        skPreferencesDataSource.setNewsResourceBookmarked(newsResourceId, bookmarked)
        analyticsHelper.logNewsResourceBookmarkToggled(
            newsResourceId = newsResourceId,
            isBookmarked = bookmarked,
        )
    }

    override suspend fun setNewsResourceViewed(newsResourceId: String, viewed: Boolean) =
        skPreferencesDataSource.setNewsResourceViewed(newsResourceId, viewed)

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        skPreferencesDataSource.setThemeBrand(themeBrand)
        analyticsHelper.logThemeChanged(themeBrand.name)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        skPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
        analyticsHelper.logDarkThemeConfigChanged(darkThemeConfig.name)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        skPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
        analyticsHelper.logDynamicColorPreferenceChanged(useDynamicColor)
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        skPreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
        analyticsHelper.logOnboardingStateChanged(shouldHideOnboarding)
    }
}
