package com.ashish.stash.domain.usecase

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.ashish.stash.core.database.entity.DocumentEntity
import com.ashish.stash.core.database.entity.Importance
import com.ashish.stash.core.database.entity.OcrStatus
import com.ashish.stash.core.database.entity.SourceKind
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.hash.HashService
import com.ashish.stash.core.saf.SafUriManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ImportDocumentUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: DocumentRepository,
    private val safUriManager: SafUriManager,
    private val hashService: HashService
) {
    suspend operator fun invoke(uri: Uri, source: String = "MANUAL"): Long = withContext(Dispatchers.IO) {
        // 1. Get Metadata
        val metadata = safUriManager.queryMetadata(uri) ?: return@withContext -1L
        
        // 2. Persist Permission or Copy locally
        val isShared = uri.authority?.contains("com.ashish.stash") == false && !uri.toString().startsWith("content://com.android.externalstorage")
        
        val finalUri: String
        val sourceKind: SourceKind
        
        if (isShared && source == "SHARE") {
            val localFile = copyToInternal(uri, metadata.filename) ?: return@withContext -1L
            finalUri = Uri.fromFile(localFile).toString()
            sourceKind = SourceKind.LOCAL_COPY
        } else {
            safUriManager.takePersistablePermission(uri)
            finalUri = uri.toString()
            sourceKind = SourceKind.SAF_REFERENCE
        }

        // 3. Compute Real Hash
        val fileHash = hashService.calculateHash(uri)
        
        // 4. Duplicate Check (Hash + Size)
        val existingCount = repository.countDocumentsByHashAndSize(fileHash, metadata.size)
        if (existingCount > 0) return@withContext -2L // Duplicate code

        // 5. Insert
        val doc = DocumentEntity(
            uri = finalUri,
            displayTitle = metadata.filename,
            originalFilename = metadata.filename,
            mimeType = metadata.mimeType,
            fileHash = fileHash,
            fileSize = metadata.size,
            importance = Importance.MEDIUM,
            ocrStatus = OcrStatus.NONE,
            sourceKind = sourceKind,
            importSource = source,
            createdAt = System.currentTimeMillis(),
            lastOpenedAt = System.currentTimeMillis()
        )
        
        repository.insertDocument(doc)
    }

    private fun copyToInternal(uri: Uri, filename: String): File? {
        val importsDir = File(context.filesDir, "imports").apply { if (!exists()) mkdirs() }
        val destFile = File(importsDir, "${System.currentTimeMillis()}_$filename")
        
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            destFile
        } catch (e: Exception) {
            null
        }
    }
}
