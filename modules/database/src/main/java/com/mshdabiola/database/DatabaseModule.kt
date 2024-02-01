/*
 *abiola 2024
 */

package com.mshdabiola.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun databaseProvider(
        @ApplicationContext context: Context,
    ): SkeletonDatabase {
        return Room.databaseBuilder(context, SkeletonDatabase::class.java, "skeletonDb.db")
            .build()
        //        return Room.inMemoryDatabaseBuilder(context,LudoDatabase::class.java,)
        //            .build()
    }
}
