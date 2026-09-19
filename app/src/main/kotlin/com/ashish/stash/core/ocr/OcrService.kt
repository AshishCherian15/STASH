package com.ashish.stash.core.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OcrService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun extractText(uri: Uri): String? {
        val mimeType = context.contentResolver.getType(uri) ?: ""
        return if (mimeType.contains("pdf")) {
            extractTextFromPdf(uri)
        } else {
            extractTextFromImage(uri)
        }
    }

    private suspend fun extractTextFromImage(uri: Uri): String? {
        return try {
            val image = InputImage.fromFilePath(context, uri)
            val result = recognizer.process(image).await()
            result.text.takeIf { it.isNotBlank() }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun extractTextFromPdf(uri: Uri): String? {
        return try {
            val pfd = context.contentResolver.openFileDescriptor(uri, "r") ?: return null
            val renderer = PdfRenderer(pfd)
            val textBuilder = StringBuilder()
            
            // Limit to first 20 pages to prevent heavy processing
            val pageCount = renderer.pageCount.coerceAtMost(20)
            
            for (i in 0 until pageCount) {
                renderer.openPage(i).use { page ->
                    // Standard document width (~1600px)
                    val width = 1600
                    val height = (width * page.height / page.width)
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    
                    val canvas = Canvas(bitmap)
                    canvas.drawColor(Color.WHITE)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    
                    val image = InputImage.fromBitmap(bitmap, 0)
                    val result = recognizer.process(image).await()
                    textBuilder.append(result.text).append("\n")
                    
                    bitmap.recycle()
                }
            }
            
            renderer.close()
            pfd.close()
            
            textBuilder.toString().takeIf { it.isNotBlank() }
        } catch (e: Exception) {
            null
        }
    }
}
