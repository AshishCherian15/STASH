package com.ashish.stash.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ashish.stash.core.database.entity.FolderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(folder: FolderEntity): Long

    @Update
    suspend fun update(folder: FolderEntity)

    @Delete
    suspend fun delete(folder: FolderEntity)

    @Query("SELECT * FROM folders WHERE folder_id = :id")
    suspend fun getById(id: Long): FolderEntity?

    @Query("UPDATE folders SET is_locked = :isLocked WHERE folder_id = :id")
    suspend fun updateLockStatus(id: Long, isLocked: Int)

    @Query("SELECT * FROM folders WHERE (:showLocked = 1 OR is_locked = 0) ORDER BY name ASC")
    fun observeAll(showLocked: Int = 0): Flow<List<FolderEntity>>

    @Query("""
        SELECT * FROM folders 
        WHERE (parent_folder_id = :parentFolderId OR (:parentFolderId IS NULL AND parent_folder_id IS NULL)) 
          AND (:showLocked = 1 OR is_locked = 0) 
        ORDER BY name ASC
    """)
    fun observeSubfolders(parentFolderId: Long?, showLocked: Int = 0): Flow<List<FolderEntity>>
}
