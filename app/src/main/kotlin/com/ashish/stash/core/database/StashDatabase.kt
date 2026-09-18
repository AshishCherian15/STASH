package com.ashish.stash.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ashish.stash.core.database.dao.CategoryDao
import com.ashish.stash.core.database.dao.DocumentDao
import com.ashish.stash.core.database.dao.DocumentSearchDao
import com.ashish.stash.core.database.dao.FolderDao
import com.ashish.stash.core.database.dao.LabelDao
import com.ashish.stash.core.database.dao.ResourceLinkDao
import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.core.database.entity.DocumentEntity
import com.ashish.stash.core.database.entity.DocumentFtsEntity
import com.ashish.stash.core.database.entity.DocumentLabelEntity
import com.ashish.stash.core.database.entity.FolderEntity
import com.ashish.stash.core.database.entity.LabelEntity
import com.ashish.stash.core.database.entity.ResourceLinkEntity

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
    version = 1,
    exportSchema = true
)
abstract class StashDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
    abstract fun categoryDao(): CategoryDao
    abstract fun folderDao(): FolderDao
    abstract fun labelDao(): LabelDao
    abstract fun documentSearchDao(): DocumentSearchDao
    abstract fun resourceLinkDao(): ResourceLinkDao
}
