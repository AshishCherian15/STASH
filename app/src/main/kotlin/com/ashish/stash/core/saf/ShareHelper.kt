package com.ashish.stash.core.saf

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.ashish.stash.core.database.entity.DocumentEntity
import java.io.File

object ShareHelper {
    fun createShareIntent(ctx: Context, docs: List<DocumentEntity>): Intent {
        if (docs.isEmpty()) return Intent()

        val uris = docs.map { d ->
            val u = Uri.parse(d.uri)
            if (u.scheme == "file") {
                // Convert internal file paths to secure content:// URIs
                FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", File(u.path!!))
            } else {
                u
            }
        }

        val mimeTypes = docs.map { it.mimeType }.distinct()
        val commonMime = when {
            mimeTypes.size == 1 -> mimeTypes[0]
            mimeTypes.all { it.startsWith("image/") } -> "image/*"
            mimeTypes.all { it.startsWith("text/") } -> "text/*"
            else -> "*/*"
        }

        return Intent(if (uris.size == 1) Intent.ACTION_SEND else Intent.ACTION_SEND_MULTIPLE).apply {
            type = commonMime
            if (uris.size == 1) {
                putExtra(Intent.EXTRA_STREAM, uris[0])
            } else {
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
            }
            
            // Critical for Android 10+ sharing reliability
            clipData = ClipData.newRawUri("Documents", uris[0]).apply {
                uris.drop(1).forEach { addItem(ClipData.Item(it)) }
            }
            
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}
