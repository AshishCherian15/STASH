package com.ashish.stash.domain.usecase

import android.content.Context
import android.net.Uri
import com.ashish.stash.core.database.entity.SourceKind
import com.ashish.stash.core.database.repository.DocumentRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class PhysicalLockDocumentUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(documentId: Long, lock: Boolean): Boolean = withContext(Dispatchers.IO) {
        val document = repository.getDocumentById(documentId) ?: return@withContext false
        if (document.isLocked == lock) return@withContext true

        val currentUri = Uri.parse(document.uri)
        
        if (lock) {
            val localFile = copyToInternal(currentUri, document.originalFilename)
            if (localFile != null) {
                repository.updateDocument(document.copy(
                    uri = Uri.fromFile(localFile).toString(),
                    sourceKind = SourceKind.LOCAL_COPY,
                    isLocked = true
                ))
                true
            } else false
        } else {
            repository.updateDocumentLockStatus(documentId, false)
            true
        }
    }

    private fun copyToInternal(uri: Uri, filename: String): File? {
        val vaultDir = File(context.filesDir, "vault").apply { if (!exists()) mkdirs() }
        val destFile = File(vaultDir, "${System.currentTimeMillis()}_$filename")
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
