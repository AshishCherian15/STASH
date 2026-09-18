package com.ashish.stash.worker.scanner

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.database.entity.DocumentEntity
import com.ashish.stash.core.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@HiltWorker
class BackgroundScannerWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: DocumentRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val root = Environment.getExternalStorageDirectory()
            val newFilesCount = scanDirectory(root)
            
            if (newFilesCount > 0) {
                notificationHelper.showAlert(
                    "Sync Complete",
                    "Found $newFilesCount new documents on your device."
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun scanDirectory(dir: File): Int {
        var count = 0
        val files = dir.listFiles() ?: return 0
        
        val now = System.currentTimeMillis()
        for (file in files) {
            if (file.isDirectory) {
                if (!file.name.startsWith(".")) {
                    count += scanDirectory(file)
                }
            } else {
                if (isSupportedFile(file)) {
                    val uri = Uri.fromFile(file)
                    // Simple dedup by name and size for auto-scan performance
                    val isDuplicate = repository.countDocumentsByHashAndSize("", file.length()) > 0
                    
                    if (!isDuplicate) {
                        repository.insertDocument(DocumentEntity(
                            uri = uri.toString(),
                            displayTitle = file.name,
                            originalFilename = file.name,
                            mimeType = getMimeType(file),
                            fileHash = "",
                            fileSize = file.length(),
                            importance = "NORMAL",
                            isLocked = 0,
                            createdAt = now,
                            lastOpenedAt = now,
                            importSource = "AUTO_SCAN"
                        ))
                        count++
                    }
                }
            }
        }
        return count
    }

    private fun isSupportedFile(file: File): Boolean {
        val extension = file.extension.lowercase()
        return extension in listOf("pdf", "jpg", "jpeg", "png", "txt", "md")
    }

    private fun getMimeType(file: File): String {
        val extension = file.extension.lowercase()
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "application/octet-stream"
    }
}
