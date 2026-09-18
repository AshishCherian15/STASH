package com.ashish.stash.di

import android.content.Context
import androidx.room.Room
import com.ashish.stash.core.database.StashDatabase
import com.ashish.stash.core.database.dao.CategoryDao
import com.ashish.stash.core.database.dao.DocumentDao
import com.ashish.stash.core.database.dao.DocumentSearchDao
import com.ashish.stash.core.database.dao.FolderDao
import com.ashish.stash.core.database.dao.LabelDao
import com.ashish.stash.core.database.dao.ResourceLinkDao
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
        ).fallbackToDestructiveMigration().build()
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
