/*
 *abiola 2024
 */

package com.mshdabiola.skeletonandroid

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SkeletonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (packageName.contains("debug")) {
            Timber.plant(Timber.DebugTree())
            Timber.e("log on app create")
        }
    }
}
