package com.ashish.stash.core.work

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.ashish.stash.core.database.entity.OcrStatus
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.ocr.OcrService
import com.ashish.stash.core.util.Result as StashResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class OcrProcessingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: DocumentRepository,
    private val ocrService: OcrService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): ListenableWorker.Result {
        val documentId = inputData.getLong("document_id", -1L)
        if (documentId == -1L) return ListenableWorker.Result.failure()

        if (runAttemptCount > 3) {
            repository.updateDocumentOcrResult(documentId, null, OcrStatus.FAILED)
            return ListenableWorker.Result.failure()
        }

        val document = repository.getDocumentById(documentId) ?: return ListenableWorker.Result.failure()
        
        repository.updateDocumentOcrResult(documentId, null, OcrStatus.PENDING)
        
        val uri = Uri.parse(document.uri)
        val ocrResult = ocrService.extractText(uri)
        
        return when (ocrResult) {
            is StashResult.Success -> {
                repository.updateDocumentOcrResult(documentId, ocrResult.data, OcrStatus.DONE)
                ListenableWorker.Result.success()
            }
            is StashResult.Error -> {
                if (runAttemptCount >= 2) {
                    repository.updateDocumentOcrResult(documentId, null, OcrStatus.FAILED)
                    ListenableWorker.Result.success()
                } else {
                    ListenableWorker.Result.retry()
                }
            }
            else -> ListenableWorker.Result.retry()
        }
    }
}
