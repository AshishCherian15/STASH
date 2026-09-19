package com.ashish.stash.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ashish.stash.core.database.MIGRATION_1_2
import com.ashish.stash.core.database.StashDatabase
import com.ashish.stash.core.database.dao.*
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
    fun provideStashDatabase(
        @ApplicationContext context: Context
    ): StashDatabase {
        return Room.databaseBuilder(
            context,
            StashDatabase::class.java,
            "stash_database"
        )
        .addMigrations(MIGRATION_1_2)
        .build()
    }

    @Provides
    fun provideDocumentDao(database: StashDatabase): DocumentDao = database.documentDao()

    @Provides
    fun provideCategoryDao(database: StashDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideFolderDao(database: StashDatabase): FolderDao = database.folderDao()

    @Provides
    fun provideLabelDao(database: StashDatabase): LabelDao = database.labelDao()

    @Provides
    fun provideDocumentSearchDao(database: StashDatabase): DocumentSearchDao = database.documentSearchDao()

    @Provides
    fun provideResourceLinkDao(database: StashDatabase): ResourceLinkDao = database.resourceLinkDao()
}
