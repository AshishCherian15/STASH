package com.ashish.stash.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ashish.stash.core.database.entity.ResourceLinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResourceLinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resourceLink: ResourceLinkEntity): Long

    @Update
    suspend fun update(resourceLink: ResourceLinkEntity)

    @Delete
    suspend fun delete(resourceLink: ResourceLinkEntity)

    @Query("SELECT * FROM resource_links WHERE document_id = :documentId")
    suspend fun getLinksForDocument(documentId: Long): List<ResourceLinkEntity>

    @Query("SELECT * FROM resource_links WHERE document_id = :documentId")
    fun observeLinksForDocument(documentId: Long): Flow<List<ResourceLinkEntity>>
}
