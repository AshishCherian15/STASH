package com.ashish.stash.ui.feature.viewer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PdfViewer(uri: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bitmaps = remember(uri) { mutableStateListOf<Bitmap>() }

    LaunchedEffect(uri) {
        withContext(Dispatchers.IO) {
            try {
                val fileUri = Uri.parse(uri)
                val pfd = context.contentResolver.openFileDescriptor(fileUri, "r") ?: return@withContext
                val renderer = PdfRenderer(pfd)
                
                for (i in 0 until renderer.pageCount) {
                    val page = renderer.openPage(i)
                    // High quality: 3x density for better clarity
                    val bitmap = Bitmap.createBitmap(page.width * 3, page.height * 3, Bitmap.Config.ARGB_8888)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    withContext(Dispatchers.Main) {
                        bitmaps.add(bitmap)
                    }
                    page.close()
                }
                renderer.close()
                pfd.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(bitmaps.size) { index ->
            Image(
                bitmap = bitmaps[index].asImageBitmap(),
                contentDescription = "Page ${index + 1}",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
