/*
 *abiola 2024
 */

package com.mshdabiola.database

import com.mshdabiola.database.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun noteDaoProvider(db: SkeletonDatabase): NoteDao {
        return db.getNoteDao()
    }
}
