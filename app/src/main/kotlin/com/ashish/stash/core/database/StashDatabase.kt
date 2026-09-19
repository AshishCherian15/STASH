package com.ashish.stash.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ashish.stash.core.database.dao.*
import com.ashish.stash.core.database.entity.*

@Database(
    entities = [
        DocumentEntity::class,
        DocumentFtsEntity::class,
        CategoryEntity::class,
        FolderEntity::class,
        LabelEntity::class,
        DocumentLabelEntity::class,
        ResourceLinkEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(StashTypeConverters::class)
abstract class StashDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
    abstract fun categoryDao(): CategoryDao
    abstract fun folderDao(): FolderDao
    abstract fun labelDao(): LabelDao
    abstract fun documentSearchDao(): DocumentSearchDao
    abstract fun resourceLinkDao(): ResourceLinkDao
}
