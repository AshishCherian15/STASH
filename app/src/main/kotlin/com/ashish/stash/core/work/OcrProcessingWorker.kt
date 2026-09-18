package com.ashish.stash.core.work

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
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

        val document = repository.getDocumentById(documentId) ?: return Result.failure()
        val uri = Uri.parse(document.uri)

        val extractedText = ocrService.extractText(uri)
        if (extractedText != null) {
            val updatedDocument = document.copy(ocrText = extractedText)
            repository.updateDocument(updatedDocument)
            return Result.success()
        }

        return Result.retry()
    }
}
