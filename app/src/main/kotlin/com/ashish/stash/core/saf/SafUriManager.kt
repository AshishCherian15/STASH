package com.ashish.stash.core.saf

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SafUriManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    data class SafMetadata(
        val filename: String,
        val size: Long,
        val mimeType: String
    )

    fun queryMetadata(uri: Uri): SafMetadata? {
        val contentResolver = context.contentResolver
        return try {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    
                    val name = if (nameIndex != -1) cursor.getString(nameIndex) else "Unknown"
                    val size = if (sizeIndex != -1) cursor.getLong(sizeIndex) else 0L
                    val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
                    
                    SafMetadata(name, size, mimeType)
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun renameDocument(uri: Uri, newName: String): Uri? {
        return try {
            DocumentsContract.renameDocument(context.contentResolver, uri, newName)
        } catch (e: Exception) {
            null
        }
    }

    fun takePersistablePermission(uri: Uri) {
        val contentResolver = context.contentResolver
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        try {
            contentResolver.takePersistableUriPermission(uri, takeFlags)
        } catch (e: Exception) {
            // Handle gracefully
        }
    }

    fun releasePersistablePermission(uri: Uri) {
        val contentResolver = context.contentResolver
        val releaseFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        try {
            contentResolver.releasePersistableUriPermission(uri, releaseFlags)
        } catch (e: Exception) {
            // Handle gracefully
        }
    }

    fun isUriAccessible(uri: Uri): Boolean {
        if (uri.scheme == "file") return File(uri.path!!).exists()
        return try {
            context.contentResolver.persistedUriPermissions.any { it.uri == uri && it.isReadPermission }
        } catch (e: Exception) {
            false
        }
    }
}
