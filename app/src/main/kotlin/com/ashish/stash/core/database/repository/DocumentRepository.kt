package com.ashish.stash.core.database.repository

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ashish.stash.core.database.dao.*
import com.ashish.stash.core.database.entity.*
import com.ashish.stash.core.hash.HashService
import com.ashish.stash.core.saf.SafUriManager
import com.ashish.stash.core.work.OcrProcessingWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface DocumentRepository {
    suspend fun insertDocument(document: DocumentEntity): Long
    suspend fun updateDocument(document: DocumentEntity)
    suspend fun deleteDocument(id: Long)
    suspend fun checkAndInjectDefaults()
    suspend fun getDocumentById(id: Long): DocumentEntity?
    suspend fun getDocumentWithMetadataById(id: Long, showLocked: Boolean = false): DocumentWithMetadata?
    fun observeAllDocuments(showLocked: Boolean = false): Flow<List<DocumentWithMetadata>>
    suspend fun countDocumentsByHashAndSize(hash: String, size: Long): Int
    fun observePriorityDocumentsWithMetadata(showLocked: Boolean = false): Flow<List<DocumentWithMetadata>>
    fun observeUnlockedDocumentsCount(): Flow<Int>
    fun observeLockedDocumentsCount(): Flow<Int>
    fun observeTotalSizeBytes(showLocked: Boolean = false): Flow<Long?>
    suspend fun updateDocumentLastOpened(id: Long, timestamp: Long): Int
    suspend fun updateDocumentLockStatus(id: Long, isLocked: Boolean)
    suspend fun updateDocumentDescription(id: Long, description: String?)
    suspend fun updateDocumentImportance(id: Long, importance: Importance)
    suspend fun updateDocumentOcrResult(id: Long, text: String?, status: OcrStatus)
    suspend fun updateDocumentCategory(id: Long, categoryId: Long?)
    suspend fun updateDocumentFolder(id: Long, folderId: Long?)
    suspend fun renameDocumentPhysical(id: Long, newName: String): Boolean

    // Search
    fun searchDocuments(searchQuery: String, showLocked: Boolean = false): Flow<List<DocumentWithMetadata>>

    // Categories
    suspend fun insertCategory(category: CategoryEntity): Long
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategory(category: CategoryEntity)
    fun observeAllCategories(): Flow<List<CategoryEntity>>

    // Folders
    suspend fun insertFolder(folder: FolderEntity): Long
    suspend fun updateFolder(folder: FolderEntity)
    suspend fun deleteFolder(folder: FolderEntity)
    suspend fun updateFolderLockStatus(id: Long, isLocked: Int)
    suspend fun getFolderById(id: Long): FolderEntity?
    fun observeAllFolders(showLocked: Boolean = false): Flow<List<FolderEntity>>

    // Labels
    suspend fun insertLabel(label: LabelEntity): Long
    suspend fun updateLabel(label: LabelEntity)
    suspend fun deleteLabel(label: LabelEntity)
    fun observeAllLabels(): Flow<List<LabelEntity>>
    suspend fun insertDocumentLabel(documentLabel: DocumentLabelEntity)
    suspend fun deleteDocumentLabel(documentId: Long, labelId: Long)
    fun observeLabelsForDocument(documentId: Long, showLocked: Boolean = false): Flow<List<LabelEntity>>

    // Resource Links
    suspend fun insertResourceLink(resourceLink: ResourceLinkEntity): Long
    suspend fun deleteResourceLink(resourceLink: ResourceLinkEntity)
    fun observeLinksForDocument(documentId: Long): Flow<List<ResourceLinkEntity>>
}

@Singleton
class DocumentRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val documentDao: DocumentDao,
    private val categoryDao: CategoryDao,
    private val folderDao: FolderDao,
    private val labelDao: LabelDao,
    private val documentSearchDao: DocumentSearchDao,
    private val resourceLinkDao: ResourceLinkDao,
    private val safUriManager: SafUriManager,
    private val hashService: HashService
) : DocumentRepository {

    private fun Boolean.toInt() = if (this) 1 else 0

    override suspend fun insertDocument(document: DocumentEntity): Long = withContext(Dispatchers.IO) {
        val id = documentDao.insert(document)
        enqueueOcrIfNeeded(id, document.mimeType)
        id
    }

    private fun enqueueOcrIfNeeded(documentId: Long, mimeType: String) {
        if (mimeType.startsWith("image/") || mimeType.contains("pdf")) {
            val workRequest = OneTimeWorkRequestBuilder<OcrProcessingWorker>()
                .setInputData(Data.Builder().putLong("document_id", documentId).build())
                .addTag("ocr_$documentId")
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    override suspend fun updateDocument(document: DocumentEntity) = withContext(Dispatchers.IO) {
        documentDao.update(document)
    }

    override suspend fun deleteDocument(id: Long) = withContext(Dispatchers.IO) {
        val document = documentDao.getById(id)
        if (document != null) {
            WorkManager.getInstance(context).cancelAllWorkByTag("ocr_$id")
            
            if (document.sourceKind == SourceKind.LOCAL_COPY) {
                try {
                    val file = File(Uri.parse(document.uri).path!!)
                    if (file.exists()) file.delete()
                } catch (e: Exception) {}
            }
            documentDao.deleteById(id)
        }
    }

    override suspend fun checkAndInjectDefaults() = withContext(Dispatchers.IO) {
        val existing = categoryDao.observeAll().first()
        if (existing.isEmpty()) {
            StashDefaults.Categories.forEach { categoryDao.insert(it) }
            StashDefaults.Folders.forEach { folderDao.insert(it) }
            StashDefaults.Labels.forEach { labelDao.insert(it) }
        }
    }

    override suspend fun getDocumentById(id: Long): DocumentEntity? = withContext(Dispatchers.IO) {
        documentDao.getById(id)
    }

    override suspend fun getDocumentWithMetadataById(id: Long, showLocked: Boolean): DocumentWithMetadata? = withContext(Dispatchers.IO) {
        documentDao.getWithMetadataById(id, showLocked.toInt())
    }

    override fun observeAllDocuments(showLocked: Boolean): Flow<List<DocumentWithMetadata>> =
        documentDao.observeAll(showLocked.toInt())

    override suspend fun countDocumentsByHashAndSize(hash: String, size: Long): Int = withContext(Dispatchers.IO) {
        documentDao.countByHashAndSize(hash, size)
    }

    override fun observePriorityDocumentsWithMetadata(showLocked: Boolean): Flow<List<DocumentWithMetadata>> =
        documentDao.observePriorityWithMetadata(showLocked.toInt())

    override fun observeUnlockedDocumentsCount(): Flow<Int> = documentDao.observeUnlockedCount()
    override fun observeLockedDocumentsCount(): Flow<Int> = documentDao.observeLockedCount()
    override fun observeTotalSizeBytes(showLocked: Boolean): Flow<Long?> = documentDao.observeTotalSizeBytes(showLocked.toInt())

    override suspend fun updateDocumentLastOpened(id: Long, timestamp: Long): Int = withContext(Dispatchers.IO) {
        documentDao.updateLastOpened(id, timestamp)
    }

    override suspend fun updateDocumentLockStatus(id: Long, isLocked: Boolean) = withContext(Dispatchers.IO) {
        documentDao.updateLockStatus(id, isLocked)
        Unit
    }

    override suspend fun updateDocumentDescription(id: Long, description: String?) = withContext(Dispatchers.IO) {
        documentDao.updateDescription(id, description)
        Unit
    }

    override suspend fun updateDocumentImportance(id: Long, importance: Importance) = withContext(Dispatchers.IO) {
        documentDao.updateImportance(id, importance)
        Unit
    }

    override suspend fun updateDocumentOcrResult(id: Long, text: String?, status: OcrStatus) = withContext(Dispatchers.IO) {
        documentDao.updateOcrResult(id, text, status)
        Unit
    }

    override suspend fun updateDocumentCategory(id: Long, categoryId: Long?) = withContext(Dispatchers.IO) {
        documentDao.updateCategory(id, categoryId)
        Unit
    }

    override suspend fun updateDocumentFolder(id: Long, folderId: Long?) = withContext(Dispatchers.IO) {
        documentDao.updateFolder(id, folderId)
        Unit
    }

    override suspend fun renameDocumentPhysical(id: Long, newName: String): Boolean = withContext(Dispatchers.IO) {
        val document = documentDao.getById(id) ?: return@withContext false
        val uri = Uri.parse(document.uri)
        val newUri = safUriManager.renameDocument(uri, newName)
        
        if (newUri != null) {
            documentDao.updateTitle(id, newName)
            true
        } else {
            false
        }
    }

    override fun searchDocuments(searchQuery: String, showLocked: Boolean): Flow<List<DocumentWithMetadata>> =
        documentSearchDao.searchDocuments(searchQuery, showLocked.toInt())

    override suspend fun insertCategory(category: CategoryEntity): Long = withContext(Dispatchers.IO) {
        categoryDao.insert(category)
    }

    override suspend fun updateCategory(category: CategoryEntity) = withContext(Dispatchers.IO) {
        categoryDao.update(category)
    }

    override suspend fun deleteCategory(category: CategoryEntity) = withContext(Dispatchers.IO) {
        categoryDao.delete(category)
    }

    override fun observeAllCategories(): Flow<List<CategoryEntity>> = categoryDao.observeAll()

    override suspend fun insertFolder(folder: FolderEntity): Long = withContext(Dispatchers.IO) {
        folderDao.insert(folder)
    }

    override suspend fun updateFolder(folder: FolderEntity) = withContext(Dispatchers.IO) {
        folderDao.update(folder)
    }

    override suspend fun deleteFolder(folder: FolderEntity) = withContext(Dispatchers.IO) {
        folderDao.delete(folder)
    }

    override suspend fun updateFolderLockStatus(id: Long, isLocked: Int) = withContext(Dispatchers.IO) {
        folderDao.updateLockStatus(id, isLocked)
    }

    override suspend fun getFolderById(id: Long): FolderEntity? = withContext(Dispatchers.IO) {
        folderDao.getById(id)
    }

    override fun observeAllFolders(showLocked: Boolean): Flow<List<FolderEntity>> =
        folderDao.observeAll(showLocked.toInt())

    override suspend fun insertLabel(label: LabelEntity): Long = withContext(Dispatchers.IO) {
        labelDao.insert(label)
    }

    override suspend fun updateLabel(label: LabelEntity) = withContext(Dispatchers.IO) {
        labelDao.update(label)
    }

    override suspend fun deleteLabel(label: LabelEntity) = withContext(Dispatchers.IO) {
        labelDao.delete(label)
    }

    override fun observeAllLabels(): Flow<List<LabelEntity>> = labelDao.observeAll()

    override suspend fun insertDocumentLabel(documentLabel: DocumentLabelEntity) = withContext(Dispatchers.IO) {
        labelDao.insertDocumentLabel(documentLabel)
    }

    override suspend fun deleteDocumentLabel(documentId: Long, labelId: Long) = withContext(Dispatchers.IO) {
        labelDao.deleteDocumentLabel(documentId, labelId)
    }

    override fun observeLabelsForDocument(documentId: Long, showLocked: Boolean): Flow<List<LabelEntity>> =
        labelDao.observeLabelsForDocument(documentId, showLocked.toInt())

    override suspend fun insertResourceLink(resourceLink: ResourceLinkEntity): Long = withContext(Dispatchers.IO) {
        resourceLinkDao.insert(resourceLink)
    }

    override suspend fun deleteResourceLink(resourceLink: ResourceLinkEntity) = withContext(Dispatchers.IO) {
        resourceLinkDao.delete(resourceLink)
    }

    override fun observeLinksForDocument(documentId: Long): Flow<List<ResourceLinkEntity>> = 
        resourceLinkDao.observeLinksForDocument(documentId)
}
