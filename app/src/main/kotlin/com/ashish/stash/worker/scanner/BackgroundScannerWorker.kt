package com.ashish.stash.worker.scanner

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.notification.NotificationHelper
import com.ashish.stash.domain.usecase.ImportDocumentUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class BackgroundScannerWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val importDocumentUseCase: ImportDocumentUseCase,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val persistedUris = context.contentResolver.persistedUriPermissions
            var totalNewFiles = 0

            persistedUris.filter { it.isReadPermission && it.uri.toString().contains("tree") }.forEach { permission ->
                totalNewFiles += scanTree(permission.uri)
            }
            
            if (totalNewFiles > 0) {
                notificationHelper.showAlert(
                    "Sync Complete",
                    "Found $totalNewFiles new documents in your watched folders."
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun scanTree(treeUri: Uri): Int {
        var count = 0
        val rootFile = DocumentFile.fromTreeUri(context, treeUri) ?: return 0
        
        val stack = mutableListOf<DocumentFile>(rootFile)
        while (stack.isNotEmpty()) {
            val currentDir = stack.removeAt(stack.size - 1)
            val files = currentDir.listFiles()
            
            for (file in files) {
                if (file.isDirectory) {
                    stack.add(file)
                } else if (isSupported(file)) {
                    val result = importDocumentUseCase(file.uri, source = "FOLDER_MONITOR")
                    if (result >= 0) count++
                }
            }
        }
        return count
    }

    private fun isSupported(file: DocumentFile): Boolean {
        val name = file.name ?: ""
        val ext = name.substringAfterLast(".", "").lowercase()
        return ext in listOf("pdf", "jpg", "jpeg", "png", "txt", "md")
    }
}
