package com.ashish.stash.core.work

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ashish.stash.core.database.entity.OcrStatus
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.ocr.OcrService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class OcrProcessingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: DocumentRepository,
    private val ocrService: OcrService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val documentId = inputData.getLong("document_id", -1L)
        if (documentId == -1L) return Result.failure()

        // 1. Cap retries
        if (runAttemptCount > 3) {
            repository.updateDocumentOcrResult(documentId, null, OcrStatus.FAILED)
            return Result.failure()
        }

        val document = repository.getDocumentById(documentId) ?: return Result.failure()
        
        repository.updateDocumentOcrResult(documentId, null, OcrStatus.PENDING)
        
        val uri = Uri.parse(document.uri)
        val extractedText = ocrService.extractText(uri)
        
        return if (extractedText != null) {
            repository.updateDocumentOcrResult(documentId, extractedText, OcrStatus.DONE)
            Result.success()
        } else {
            // If it's a transient failure, retry. If it's a "no text found" blank image, FAILED.
            // For now, we'll mark as DONE with null text if it consistently returns null.
            if (runAttemptCount >= 2) {
                repository.updateDocumentOcrResult(documentId, null, OcrStatus.NONE)
                Result.success()
            } else {
                Result.retry()
            }
        }
    }
}
