package com.ashish.stash.core.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import com.ashish.stash.core.util.Result
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OcrService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun extractText(uri: Uri): Result<String> = coroutineScope {
        try {
            val mimeType = context.contentResolver.getType(uri) ?: ""
            val text = if (mimeType.contains("pdf")) {
                extractTextFromPdf(uri)
            } else {
                extractTextFromImage(uri)
            }
            
            if (text != null) {
                Result.Success(text)
            } else {
                Result.Error(Exception("No text found"), "File contains no readable text")
            }
        } catch (e: SecurityException) {
            Result.Error(e, "Permission denied: Cannot read file")
        } catch (e: Exception) {
            Result.Error(e, "OCR failed: ${e.localizedMessage}")
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

    private suspend fun extractTextFromPdf(uri: Uri): String? = coroutineScope {
        try {
            val pfd = context.contentResolver.openFileDescriptor(uri, "r") ?: return@coroutineScope null
            val renderer = PdfRenderer(pfd)
            val textBuilder = StringBuilder()
            
            // Limit to 100 pages with adaptive sizing
            val pageCount = renderer.pageCount.coerceAtMost(100)
            
            for (i in 0 until pageCount) {
                if (!isActive) break
                
                renderer.openPage(i).use { page ->
                    // Adaptive sizing based on available memory
                    val maxMemory = Runtime.getRuntime().maxMemory()
                    val targetWidth = if (maxMemory > 512_000_000) 2000 else 1200
                    
                    val width = targetWidth.coerceAtMost(page.width * 2)
                    val height = (width * page.height / page.width)
                    
                    // Use RGB_565 to save 50% memory over ARGB_8888
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    
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
